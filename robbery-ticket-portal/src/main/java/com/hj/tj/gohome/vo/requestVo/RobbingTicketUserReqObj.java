package com.hj.tj.gohome.vo.requestVo;

import lombok.Data;

import java.util.List;

@Data
public class RobbingTicketUserReqObj {
    private String name;

    private List<Integer> robbingIdList;

}
