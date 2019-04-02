package com.hj.tj.gohome.service;

import com.hj.tj.gohome.vo.passenger.PassengerDetailResObj;
import com.hj.tj.gohome.vo.passenger.PassengerResObj;
import com.hj.tj.gohome.vo.passenger.PassengerSaveReqObj;

import java.util.List;

public interface PassengerService {

    /**
     * 添加和更新旅客
     *
     * @param passengerSaveReqObj
     * @return
     */
    Integer savePassenger(PassengerSaveReqObj passengerSaveReqObj);

    /**
     * 查询当前用户关联的所有乘客
     *
     * @return
     */
    List<PassengerResObj> listPassenger();

    /**
     * 获取乘客详情
     *
     * @param id
     * @return
     */
    PassengerDetailResObj getPassengerDetail(Integer id);
}
