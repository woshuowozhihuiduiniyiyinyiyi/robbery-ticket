package com.hj.tj.gohome.vo.requestVo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author tangj
 * @description
 * @since 2019/2/26 9:56
 */
@Data
public class LoginReqObj {

    @NotBlank(message = "账号不能为空")
    private String account;

    @NotBlank(message = "密码不能为空")
    private String password;
}
