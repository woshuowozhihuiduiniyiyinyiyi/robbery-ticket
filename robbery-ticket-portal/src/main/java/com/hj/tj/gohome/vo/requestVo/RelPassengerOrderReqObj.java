package com.hj.tj.gohome.vo.requestVo;

import lombok.Data;

import java.util.List;

/**
 * @author tangj
 * @description
 * @since 2018/10/15 9:43
 */
@Data
public class RelPassengerOrderReqObj {

    private List<Integer> passengerIdList;

    private List<Integer> orderIdList;

}
