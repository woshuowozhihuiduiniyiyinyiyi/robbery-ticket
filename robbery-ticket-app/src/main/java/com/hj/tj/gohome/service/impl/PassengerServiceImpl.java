package com.hj.tj.gohome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hj.tj.gohome.entity.Passenger;
import com.hj.tj.gohome.entity.RelOwnerPassenger;
import com.hj.tj.gohome.enums.IdCardTypeEnum;
import com.hj.tj.gohome.enums.PassengerTypeEnum;
import com.hj.tj.gohome.enums.StatusEnum;
import com.hj.tj.gohome.mapper.PassengerMapper;
import com.hj.tj.gohome.mapper.RelOwnerPassengerMapper;
import com.hj.tj.gohome.service.PassengerService;
import com.hj.tj.gohome.utils.OwnerContextHelper;
import com.hj.tj.gohome.vo.passenger.PassengerResObj;
import com.hj.tj.gohome.vo.passenger.PassengerSaveReqObj;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PassengerServiceImpl implements PassengerService {

    @Resource
    private PassengerMapper passengerMapper;

    @Resource
    private RelOwnerPassengerMapper relOwnerPassengerMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer savePassenger(PassengerSaveReqObj passengerSaveReqObj) {
        Passenger passenger = new Passenger();
        BeanUtils.copyProperties(passengerSaveReqObj, passenger);
        passenger.setUpdater(OwnerContextHelper.getOwnerId().toString());

        if (Objects.nonNull(passenger.getId())) {
            // 更新
            passenger.setUpdatedAt(new Date());
            passengerMapper.updateById(passenger);
        } else {
            passenger.setCreator(OwnerContextHelper.getOwnerId().toString());
            passengerMapper.insert(passenger);
            // 插入关联表
            RelOwnerPassenger relOwnerPassenger = new RelOwnerPassenger();
            relOwnerPassenger.setOwnerId(OwnerContextHelper.getOwnerId());
            relOwnerPassenger.setCreator(OwnerContextHelper.getOwnerId().toString());
            relOwnerPassenger.setPassengerId(passenger.getId());
            relOwnerPassenger.setUpdater(OwnerContextHelper.getOwnerId().toString());

            relOwnerPassengerMapper.insert(relOwnerPassenger);
        }

        return passenger.getId();
    }

    @Override
    public List<PassengerResObj> listPassenger() {
        Integer ownerId = OwnerContextHelper.getOwnerId();

        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner_id", ownerId);
        queryWrapper.eq("status", StatusEnum.UN_DELETE.getStatus());
        List<RelOwnerPassenger> relOwnerPassengers = relOwnerPassengerMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(relOwnerPassengers)) {
            return new ArrayList<>();
        }

        List<Integer> passengerIdList = relOwnerPassengers.stream().map(RelOwnerPassenger::getPassengerId).collect(Collectors.toList());

        queryWrapper = new QueryWrapper();
        queryWrapper.in("id", passengerIdList);
        queryWrapper.eq("status", StatusEnum.UN_DELETE.getStatus());
        List<Passenger> passengerList = passengerMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(passengerList)) {
            return new ArrayList<>();
        }

        List<PassengerResObj> passengerResObjList = new ArrayList<>(passengerList.size());
        for(Passenger passenger : passengerList){
            PassengerResObj passengerResObj = genPassengerResObj(passenger);
            passengerResObjList.add(passengerResObj);
        }

        return passengerResObjList;
    }


    private PassengerResObj genPassengerResObj(Passenger passenger){
        PassengerResObj passengerResObj = new PassengerResObj();
        BeanUtils.copyProperties(passenger, passengerResObj);

        IdCardTypeEnum idCardTypeEnum = IdCardTypeEnum.getIdCardTypeEnumByType(passenger.getIdCardType());
        if(Objects.nonNull(idCardTypeEnum)){
            passengerResObj.setIdCardTypeStr(idCardTypeEnum.getDescription());
        }

        PassengerTypeEnum passengerTypeEnum = PassengerTypeEnum.getPassengerTypeEnumByType(passenger.getType());
        if(Objects.nonNull(passengerTypeEnum)){
            passengerResObj.setTypeStr(passengerTypeEnum.getDescription());
        }

        return  passengerResObj;
    }
}
