package com.hj.tj.gohome.service;

import com.hj.tj.gohome.entity.Passenger;
import com.hj.tj.gohome.vo.requestVo.PassengerInsertReqObj;
import com.hj.tj.gohome.vo.requestVo.PassengerReqObj;

import java.util.List;

/**
 * @author tangj
 * @description
 * @since 2018/10/15 9:49
 */
public interface PassengerService {

    List<Passenger> listPassenger(PassengerReqObj passengerReqObj);

    List<Integer> batchSavePassenger(List<PassengerInsertReqObj> passengerInsertReqObjs);
}
