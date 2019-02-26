package com.hj.tj.gohome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hj.tj.gohome.entity.RelPassengerOrder;
import com.hj.tj.gohome.enums.BaseStatusEnum;
import com.hj.tj.gohome.mapper.RelPassengerOrderMapper;
import com.hj.tj.gohome.service.RelPassengerOrderService;
import com.hj.tj.gohome.vo.requestVo.RelPassengerOrderReqObj;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tangj
 * @description
 * @since 2018/10/15 9:43
 */
@Service
public class RelPassengerOrderServiceImpl implements RelPassengerOrderService {

    @Resource
    private RelPassengerOrderMapper relPassengerOrderMapper;

    @Override
    public List<RelPassengerOrder> listRelPassengerOrder(RelPassengerOrderReqObj relPassengerOrderReqObj) {
        QueryWrapper<RelPassengerOrder> queryWrapper = genQueryWrapper(relPassengerOrderReqObj);

        return relPassengerOrderMapper.selectList(queryWrapper);
    }

    @Override
    public List<Integer> saveRelPassengerOrder(List<Integer> passengerIdList, Integer orderId) {
        if (CollectionUtils.isEmpty(passengerIdList) || Objects.isNull(orderId)) {
            return null;
        }

        QueryWrapper<RelPassengerOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId).eq("status", BaseStatusEnum.UN_DELETE.getValue());
        List<RelPassengerOrder> relPassengerOrders = relPassengerOrderMapper.selectList(queryWrapper);

        List<RelPassengerOrder> insertList = new ArrayList<>();
        List<RelPassengerOrder> delList = new ArrayList<>();

        if (CollectionUtils.isEmpty(relPassengerOrders)) {
            for (Integer passengerId : passengerIdList) {
                RelPassengerOrder relPassengerOrder = genRelPassengerOrder(orderId, passengerId);

                insertList.add(relPassengerOrder);
            }
        } else {
            Map<Integer, RelPassengerOrder> relPassengerOrderMap = relPassengerOrders.stream()
                    .collect(Collectors.toMap(RelPassengerOrder::getPassengerId, r -> r, (m1, m2) -> m1));

            // 在传入的参数中，不在数据库存在，则插入
            for (Integer passengerId : passengerIdList) {
                if (Objects.isNull(relPassengerOrderMap.get(passengerId))) {
                    RelPassengerOrder relPassengerOrder = genRelPassengerOrder(orderId, passengerId);

                    insertList.add(relPassengerOrder);
                }
            }

            // 在数据库中存在，不在传入的列表中，则删除
            for (RelPassengerOrder relPassengerOrder : relPassengerOrders) {
                if (!passengerIdList.contains(relPassengerOrder.getPassengerId())) {
                    delList.add(relPassengerOrder);
                }
            }
        }

        List<Integer> resultIdList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(insertList)) {
            for (RelPassengerOrder relPassengerOrder : insertList) {
                relPassengerOrderMapper.insert(relPassengerOrder);
                resultIdList.add(relPassengerOrder.getId());
            }
        }

        if (!CollectionUtils.isEmpty(delList)) {
            for (RelPassengerOrder relPassengerOrder : delList) {
                relPassengerOrder.setUpdatedAt(new Date());
                relPassengerOrder.setStatus(BaseStatusEnum.DELETE.getValue());

                relPassengerOrderMapper.updateById(relPassengerOrder);
            }
        }

        return resultIdList;
    }

    private RelPassengerOrder genRelPassengerOrder(Integer orderId, Integer passengerId) {
        RelPassengerOrder relPassengerOrder = new RelPassengerOrder();
        relPassengerOrder.setCreatedAt(new Date());
        relPassengerOrder.setOrderId(orderId);
        relPassengerOrder.setPassengerId(passengerId);
        relPassengerOrder.setStatus(BaseStatusEnum.UN_DELETE.getValue());
        relPassengerOrder.setUpdatedAt(new Date());
        return relPassengerOrder;
    }

    private QueryWrapper<RelPassengerOrder> genQueryWrapper(RelPassengerOrderReqObj relPassengerOrderReqObj) {
        QueryWrapper<RelPassengerOrder> queryWrapper = new QueryWrapper<>();

        if (!CollectionUtils.isEmpty(relPassengerOrderReqObj.getOrderIdList())) {
            queryWrapper.in("order_id", relPassengerOrderReqObj.getOrderIdList());
        }

        if (!CollectionUtils.isEmpty(relPassengerOrderReqObj.getPassengerIdList())) {
            queryWrapper.in("passenger_id", relPassengerOrderReqObj.getPassengerIdList());
        }

        queryWrapper.eq("status", BaseStatusEnum.UN_DELETE.getValue());

        return queryWrapper;
    }
}
