package com.hj.tj.gohome.vo.requestVo;

import lombok.Data;

import java.util.List;

/**
 * @author tangj
 * @description
 * @since 2018/10/15 9:50
 */
@Data
public class PassengerReqObj {

    private String idCard;

    private String name;

    private List<Integer> idList;

}
