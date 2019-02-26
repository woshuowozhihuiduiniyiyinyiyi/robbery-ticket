package com.hj.tj.gohome.vo.requestVo;

import lombok.Data;

@Data
public class OwnerInsertReqObj {

    private Integer id;

    private String wxAccount;

    private String wxNickname;

    private String phone;
}
