package com.hj.tj.gohome.service;

import com.hj.tj.gohome.entity.RelPassengerOrder;
import com.hj.tj.gohome.vo.requestVo.RelPassengerOrderReqObj;

import java.util.List;

/**
 * @author tangj
 * @description
 * @since 2018/10/15 9:41
 */
public interface RelPassengerOrderService {

    List<RelPassengerOrder> listRelPassengerOrder(RelPassengerOrderReqObj relPassengerOrderReqObj);

    List<Integer> saveRelPassengerOrder(List<Integer> passengerIdList, Integer orderId);
}
