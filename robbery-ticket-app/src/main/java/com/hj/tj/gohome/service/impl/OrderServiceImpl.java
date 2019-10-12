package com.hj.tj.gohome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hj.tj.gohome.config.handler.ServiceException;
import com.hj.tj.gohome.config.handler.ServiceExceptionEnum;
import com.hj.tj.gohome.entity.*;
import com.hj.tj.gohome.enums.StatusEnum;
import com.hj.tj.gohome.mapper.*;
import com.hj.tj.gohome.service.OrderService;
import com.hj.tj.gohome.service.WxTemplateMsgService;
import com.hj.tj.gohome.utils.DateUtil;
import com.hj.tj.gohome.utils.OwnerContextHelper;
import com.hj.tj.gohome.vo.order.OrderSaveParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private RelPassengerOrderMapper relPassengerOrderMapper;

    @Resource
    private OwnerMapper ownerMapper;

    @Resource
    private ExpectDateQueryMapper expectDateQueryMapper;

    @Resource
    private OrderExpectDateQueryMapper orderExpectDateQueryMapper;

    @Resource
    private WxTemplateMsgService wxTemplateMsgService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveOrder(OrderSaveParam orderSaveParam) {
        limitSaveOrder(orderSaveParam);

        Order order = new Order();
        BeanUtils.copyProperties(orderSaveParam, order);

        order.setCreator(OwnerContextHelper.getOwnerId().toString());
        order.setOwnerId(OwnerContextHelper.getOwnerId());
        order.setUpdater(OwnerContextHelper.getOwnerId().toString());

        Owner owner = ownerMapper.selectById(OwnerContextHelper.getOwnerId());
        if (Objects.isNull(owner)) {
            throw new ServiceException(ServiceExceptionEnum.OWNER_NOT_EXISTS);
        }

        Owner tempOwner = new Owner();
        tempOwner.setId(owner.getId());
        if (!StringUtils.isEmpty(orderSaveParam.getWxAccount())) {
            tempOwner.setWxAccount(orderSaveParam.getWxAccount());
        }
        tempOwner.setPhone(orderSaveParam.getPhone());
        tempOwner.setUpdatedAt(new Date());

        ownerMapper.updateById(tempOwner);

        if (Objects.nonNull(orderSaveParam.getPrice())) {
            Double price = orderSaveParam.getPrice() * 100;
            order.setPrice(price.intValue());
        }

        if (Objects.nonNull(order.getId())) {
            // 更新
            order.setUpdatedAt(new Date());
            order.setUpdater(OwnerContextHelper.getOwnerId().toString());
            orderMapper.updateById(order);
        } else {
            // 插入
            orderMapper.insert(order);
        }

        // 插入日期查询表
        List<Integer> expectDateIdList = insertToExpectDate(order.getExpectDate());
        insertToOrderExpectDateQuery(order.getId(), expectDateIdList);

        saveRelPassengerOrder(order.getId(), orderSaveParam.getPassengerIdList());

        wxTemplateMsgService.sendNewOrderMsg(order);

        return order.getId();
    }

    private void limitSaveOrder(OrderSaveParam orderSaveParam) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("owner_id", OwnerContextHelper.getOwnerId());
        List<Order> orderList = orderMapper.selectList(orderQueryWrapper);
        if (!CollectionUtils.isEmpty(orderList)) {
            for (Order order : orderList) {
                if (Math.abs(order.getCreatedAt().getTime() - System.currentTimeMillis()) <= 10000) {
                    throw new ServiceException(ServiceExceptionEnum.ORDER_TIME_LIMIT);
                }

                if (Objects.equals(order.getOrigin(), orderSaveParam.getOrigin())
                        && Objects.equals(order.getDestination(), orderSaveParam.getDestination())
                        && Objects.equals(order.getExpectDate(), orderSaveParam.getExpectDate())
                        && Objects.equals(order.getTrainNumber(), orderSaveParam.getTrainNumber())
                        && Objects.equals(order.getStatus(), 1)
                        && Math.abs(order.getCreatedAt().getTime() - System.currentTimeMillis()) <= 30000) {
                    throw new ServiceException(ServiceExceptionEnum.ORDER_REPEAT);
                }
            }
        }
    }

    /**
     * 插入日期查询关联表
     *
     * @param orderId               订单id
     * @param expectDateQueryIdList 日期表id
     */
    private void insertToOrderExpectDateQuery(Integer orderId, List<Integer> expectDateQueryIdList) {
        QueryWrapper<OrderExpectDateQuery> queryQueryWrapper = new QueryWrapper<>();
        queryQueryWrapper.eq("order_id", orderId);

        for (Integer expectDateId : expectDateQueryIdList) {
            OrderExpectDateQuery insertRecord = new OrderExpectDateQuery();
            insertRecord.setStatus(StatusEnum.UN_DELETE.getStatus());
            insertRecord.setOrderId(orderId);
            insertRecord.setExpectDateQueryId(expectDateId);
            insertRecord.setCreator(OwnerContextHelper.getOwnerId().toString());
            insertRecord.setUpdater(OwnerContextHelper.getOwnerId().toString());

            orderExpectDateQueryMapper.insert(insertRecord);
        }
    }

    /**
     * 插入日期表
     *
     * @param expectDateString 用户期待日期字符串
     * @return 所有用户期待日期字符串对应日期表的id
     */
    private List<Integer> insertToExpectDate(String expectDateString) {
        List<Date> expectDateList = new ArrayList<>();
        String[] expectDateStrArr = expectDateString.split("、");
        for (String expectDateStr : expectDateStrArr) {
            Date expectDate = DateUtil.formatStrToDate(expectDateStr, DateUtil.NORMAL_DATE_FORMAT);
            expectDateList.add(expectDate);
        }

        QueryWrapper<ExpectDateQuery> expectDateQueryQueryWrapper = new QueryWrapper<>();
        expectDateQueryQueryWrapper.in("expect_date", expectDateList);
        List<ExpectDateQuery> expectDateQueryList = expectDateQueryMapper.selectList(expectDateQueryQueryWrapper);

        Map<Date, ExpectDateQuery> expectDateQueryMap = expectDateQueryList.stream().collect(Collectors.toMap(ExpectDateQuery::getExpectDate, e -> e));

        List<Integer> expectDateQueryIdList = new ArrayList<>();
        for (Date expectDate : expectDateList) {
            ExpectDateQuery expectDateQuery = expectDateQueryMap.get(expectDate);
            if (Objects.isNull(expectDateQuery)) {
                expectDateQuery = new ExpectDateQuery();
                expectDateQuery.setExpectDate(expectDate);
                expectDateQuery.setCreator(OwnerContextHelper.getOwnerId().toString());
                expectDateQuery.setUpdater(OwnerContextHelper.getOwnerId().toString());

                expectDateQueryMapper.insert(expectDateQuery);
            }

            expectDateQueryIdList.add(expectDateQuery.getId());
        }

        return expectDateQueryIdList;
    }

    private void saveRelPassengerOrder(Integer orderId, List<Integer> passengerIdList) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_id", orderId);

        RelPassengerOrder relPassengerOrder = new RelPassengerOrder();
        relPassengerOrder.setStatus(StatusEnum.DELETED.getStatus());

        relPassengerOrderMapper.update(relPassengerOrder, queryWrapper);

        for (Integer passengerId : passengerIdList) {
            RelPassengerOrder tempRelPassengerOrder = new RelPassengerOrder();
            tempRelPassengerOrder.setCreator(OwnerContextHelper.getOwnerId().toString());
            tempRelPassengerOrder.setOrderId(orderId);
            tempRelPassengerOrder.setPassengerId(passengerId);
            tempRelPassengerOrder.setUpdater(OwnerContextHelper.getOwnerId().toString());

            relPassengerOrderMapper.insert(tempRelPassengerOrder);
        }
    }
}
