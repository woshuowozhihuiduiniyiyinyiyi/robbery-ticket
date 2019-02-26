package com.hj.tj.gohome.vo.requestVo;

import lombok.Data;

import java.util.List;

@Data
public class RelRobbingOrderReqObj {

    private List<Integer> robbingIdList;

    private List<Integer> orderIdList;

}
