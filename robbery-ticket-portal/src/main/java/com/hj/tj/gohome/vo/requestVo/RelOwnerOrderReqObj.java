package com.hj.tj.gohome.vo.requestVo;

import lombok.Data;

import java.util.List;

@Data
public class RelOwnerOrderReqObj {

    private List<Integer> orderIdList;

    private List<Integer> ownerIdList;

}
