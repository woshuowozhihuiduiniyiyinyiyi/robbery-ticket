package com.hj.tj.gohome.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hj.tj.gohome.config.handler.ServiceException;
import com.hj.tj.gohome.config.handler.ServiceExceptionEnum;
import com.hj.tj.gohome.entity.Passenger;
import com.hj.tj.gohome.entity.PassengerStudent;
import com.hj.tj.gohome.entity.RelOwnerPassenger;
import com.hj.tj.gohome.enums.IdCardTypeEnum;
import com.hj.tj.gohome.enums.PassengerTypeEnum;
import com.hj.tj.gohome.enums.StatusEnum;
import com.hj.tj.gohome.mapper.PassengerMapper;
import com.hj.tj.gohome.mapper.PassengerStudentMapper;
import com.hj.tj.gohome.mapper.RelOwnerPassengerMapper;
import com.hj.tj.gohome.service.PassengerService;
import com.hj.tj.gohome.utils.OwnerContextHelper;
import com.hj.tj.gohome.vo.passenger.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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

    @Resource
    private PassengerStudentMapper passengerStudentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer savePassenger(PassengerSaveReqObj passengerSaveReqObj) {
        Passenger passenger = new Passenger();
        BeanUtils.copyProperties(passengerSaveReqObj, passenger);
        passenger.setUpdater(OwnerContextHelper.getOwnerId().toString());

        boolean hasStudent = Objects.equals(passenger.getType(), PassengerTypeEnum.STUDENT.getType());
        if (hasStudent) {
            checkStudentInfo(passengerSaveReqObj.getPassengerStudentReqObj());
        }

        if (Objects.nonNull(passenger.getId())) {
            // 更新
            passenger.setUpdatedAt(new Date());
            passengerMapper.updateById(passenger);

            // 学生，修改相关信息
            if (hasStudent) {
                updatePassengerStudent(passengerSaveReqObj.getPassengerStudentReqObj(), passenger.getId());
            }
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

            // 学生，添加相关信息
            if (hasStudent) {
                insertPassengerStudent(passengerSaveReqObj.getPassengerStudentReqObj(), passenger.getId());
            }
        }

        return passenger.getId();
    }

    private void insertPassengerStudent(PassengerStudentReqObj passengerStudentReqObj, Integer passengerId) {
        PassengerStudent passengerStudent = new PassengerStudent();
        BeanUtils.copyProperties(passengerStudentReqObj, passengerStudent);
        passengerStudent.setPassengerId(passengerId);
        passengerStudent.setCreator(OwnerContextHelper.getOwnerId().toString());
        passengerStudent.setUpdater(OwnerContextHelper.getOwnerId().toString());

        passengerStudentMapper.insert(passengerStudent);
    }

    private void updatePassengerStudent(PassengerStudentReqObj passengerStudentReqObj, Integer passengerId) {
        PassengerStudent passengerStudent = new PassengerStudent();
        BeanUtils.copyProperties(passengerStudentReqObj, passengerStudent);
        passengerStudent.setPassengerId(passengerId);
        passengerStudent.setUpdater(OwnerContextHelper.getOwnerId().toString());

        passengerStudentMapper.update(passengerStudent, Wrappers.<PassengerStudent>query().lambda()
                .eq(PassengerStudent::getPassengerId, passengerStudent.getPassengerId()));
    }

    private void checkStudentInfo(PassengerStudentReqObj passengerStudentReqObj) {
        if (Objects.isNull(passengerStudentReqObj)) {
            throw new ServiceException(ServiceExceptionEnum.STUDENT_INFO_ERROR);
        }

        if (StringUtils.isEmpty(passengerStudentReqObj.getDiscountEnd())) {
            throw new ServiceException(ServiceExceptionEnum.DISCOUNT_END_ERROR);
        }

        if (StringUtils.isEmpty(passengerStudentReqObj.getDiscountStart())) {
            throw new ServiceException(ServiceExceptionEnum.DISCOUNT_START_ERROR);
        }

        if (Objects.isNull(passengerStudentReqObj.getEducationalSystem()) || passengerStudentReqObj.getEducationalSystem() < 0) {
            throw new ServiceException(ServiceExceptionEnum.EDUCATIONAL_SYSTEM_ERROR);
        }

        if (Objects.isNull(passengerStudentReqObj.getEnterYear()) || passengerStudentReqObj.getEnterYear() < 0) {
            throw new ServiceException(ServiceExceptionEnum.ENTER_YEAR_ERROR);
        }

        if (StringUtils.isEmpty(passengerStudentReqObj.getSchoolId())) {
            throw new ServiceException(ServiceExceptionEnum.SCHOOL_ID_ERROR);
        }

        if (StringUtils.isEmpty(passengerStudentReqObj.getSchoolName())) {
            throw new ServiceException(ServiceExceptionEnum.SCHOOL_NAME_ERROR);
        }

        if (StringUtils.isEmpty(passengerStudentReqObj.getStudentNo())) {
            throw new ServiceException(ServiceExceptionEnum.STUDENT_NO_ERROR);
        }
    }

    @Override
    public List<PassengerResObj> listPassenger() {
        Integer ownerId = OwnerContextHelper.getOwnerId();

        List<RelOwnerPassenger> relOwnerPassengers = relOwnerPassengerMapper.selectList(Wrappers.<RelOwnerPassenger>query().lambda()
                .eq(RelOwnerPassenger::getOwnerId, ownerId)
                .eq(RelOwnerPassenger::getStatus, StatusEnum.UN_DELETE.getStatus()));
        if (CollectionUtils.isEmpty(relOwnerPassengers)) {
            return new ArrayList<>();
        }

        List<Integer> passengerIdList = relOwnerPassengers.stream().map(RelOwnerPassenger::getPassengerId).collect(Collectors.toList());

        List<Passenger> passengerList = passengerMapper.selectList(Wrappers.<Passenger>query().lambda()
                .in(Passenger::getId, passengerIdList)
                .eq(Passenger::getStatus, StatusEnum.UN_DELETE.getStatus()));
        if (CollectionUtils.isEmpty(passengerList)) {
            return new ArrayList<>();
        }

        List<PassengerResObj> passengerResObjList = new ArrayList<>(passengerList.size());
        for (Passenger passenger : passengerList) {
            PassengerResObj passengerResObj = genPassengerResObj(passenger);
            passengerResObjList.add(passengerResObj);
        }

        return passengerResObjList;
    }

    @Override
    public PassengerDetailResObj getPassengerDetail(Integer id) {
        Passenger passenger = passengerMapper.selectById(id);
        if (Objects.isNull(passenger)) {
            return null;
        }

        PassengerDetailResObj passengerDetailResObj = new PassengerDetailResObj();
        BeanUtils.copyProperties(passenger, passengerDetailResObj);
        PassengerTypeEnum passengerTypeEnum = PassengerTypeEnum.getPassengerTypeEnumByType(passenger.getType());
        if (Objects.nonNull(passengerTypeEnum)) {
            passengerDetailResObj.setTypeStr(passengerTypeEnum.getDescription());
        }

        IdCardTypeEnum idCardType = IdCardTypeEnum.getIdCardTypeEnumByType(passenger.getIdCardType());
        if (Objects.nonNull(idCardType)) {
            passengerDetailResObj.setIdCardTypeStr(idCardType.getDescription());
        }
        if (!Objects.equals(PassengerTypeEnum.STUDENT.getType(), passenger.getType())) {
            return passengerDetailResObj;
        }

        PassengerStudent passengerStudent = passengerStudentMapper.selectOne(Wrappers.<PassengerStudent>query().lambda()
                .eq(PassengerStudent::getPassengerId, id)
                .eq(PassengerStudent::getStatus, StatusEnum.UN_DELETE.getStatus()));

        if (Objects.isNull(passengerStudent)) {
            return passengerDetailResObj;
        }

        PassengerStudentResObj passengerStudentResObj = new PassengerStudentResObj();
        BeanUtils.copyProperties(passengerStudent, passengerStudentResObj);
        passengerDetailResObj.setPassengerStudentResObj(passengerStudentResObj);

        return passengerDetailResObj;
    }


    private PassengerResObj genPassengerResObj(Passenger passenger) {
        PassengerResObj passengerResObj = new PassengerResObj();
        BeanUtils.copyProperties(passenger, passengerResObj);

        IdCardTypeEnum idCardTypeEnum = IdCardTypeEnum.getIdCardTypeEnumByType(passenger.getIdCardType());
        if (Objects.nonNull(idCardTypeEnum)) {
            passengerResObj.setIdCardTypeStr(idCardTypeEnum.getDescription());
        }

        PassengerTypeEnum passengerTypeEnum = PassengerTypeEnum.getPassengerTypeEnumByType(passenger.getType());
        if (Objects.nonNull(passengerTypeEnum)) {
            passengerResObj.setTypeStr(passengerTypeEnum.getDescription());
        }

        return passengerResObj;
    }
}
