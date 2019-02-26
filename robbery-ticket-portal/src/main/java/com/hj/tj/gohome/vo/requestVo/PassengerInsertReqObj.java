package com.hj.tj.gohome.vo.requestVo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PassengerInsertReqObj {

    @NotBlank
    private String name;

    @NotBlank
    private String idCard;

}
