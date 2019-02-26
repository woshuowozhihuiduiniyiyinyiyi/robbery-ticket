package com.hj.tj.gohome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hj.tj.gohome.entity.Passenger;
import com.hj.tj.gohome.enums.BaseStatusEnum;
import com.hj.tj.gohome.mapper.PassengerMapper;
import com.hj.tj.gohome.service.PassengerService;
import com.hj.tj.gohome.utils.StringUtil;
import com.hj.tj.gohome.vo.requestVo.PassengerInsertReqObj;
import com.hj.tj.gohome.vo.requestVo.PassengerReqObj;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tangj
 * @description
 * @since 2018/10/15 9:49
 */
@Service
public class PassengerServiceImpl implements PassengerService {

    @Resource
    private PassengerMapper passengerMapper;

    @Override
    public List<Passenger> listPassenger(PassengerReqObj passengerReqObj) {
        QueryWrapper<Passenger> queryWrapper = genQueryWrapper(passengerReqObj);

        return passengerMapper.selectList(queryWrapper);
    }

    @Override
    public List<Integer> batchSavePassenger(List<PassengerInsertReqObj> passengerInsertReqObjs) {
        List<String> idCardList = passengerInsertReqObjs.stream().map(PassengerInsertReqObj::getIdCard).collect(Collectors.toList());

        QueryWrapper<Passenger> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id_card", idCardList);
        List<Passenger> passengerList = passengerMapper.selectList(queryWrapper);

        Map<String, Passenger> passengerMap = passengerList.stream().collect(Collectors.toMap(Passenger::getIdCard, p -> p, (m1, m2) -> m1));

        List<Integer> resultIdList = new ArrayList<>();

        for (PassengerInsertReqObj passengerInsertReqObj : passengerInsertReqObjs) {
            Passenger passenger = passengerMap.get(passengerInsertReqObj.getIdCard());
            if (Objects.nonNull(passenger)) {
                // 更新
                passenger.setUpdatedAt(new Date());
                passenger.setName(passengerInsertReqObj.getName());

                passengerMapper.updateById(passenger);
                resultIdList.add(passenger.getId());
            } else {
                // 插入
                passenger = genPassenger(passengerInsertReqObj);

                passengerMapper.insert(passenger);
                resultIdList.add(passenger.getId());
            }
        }

        return resultIdList;
    }

    private Passenger genPassenger(PassengerInsertReqObj passengerInsertReqObj) {
        Passenger passenger = new Passenger();
        passenger.setName(passengerInsertReqObj.getName());
        passenger.setUpdatedAt(new Date());
        passenger.setCreatedAt(new Date());
        passenger.setIdCard(passengerInsertReqObj.getIdCard());
        passenger.setName(passengerInsertReqObj.getName());
        passenger.setStatus(BaseStatusEnum.UN_DELETE.getValue());

        return passenger;
    }

    private QueryWrapper<Passenger> genQueryWrapper(PassengerReqObj passengerReqObj) {
        QueryWrapper<Passenger> queryWrapper = new QueryWrapper<>();

        if (StringUtil.isNotBlank(passengerReqObj.getIdCard())) {
            queryWrapper.like("id_card", passengerReqObj.getIdCard());
        }

        if (StringUtil.isNotBlank(passengerReqObj.getName())) {
            queryWrapper.like("name", passengerReqObj.getName());
        }

        if (!CollectionUtils.isEmpty(passengerReqObj.getIdList())) {
            queryWrapper.in("id", passengerReqObj.getIdList());
        }

        queryWrapper.eq("status", BaseStatusEnum.UN_DELETE.getValue());

        return queryWrapper;
    }
}
