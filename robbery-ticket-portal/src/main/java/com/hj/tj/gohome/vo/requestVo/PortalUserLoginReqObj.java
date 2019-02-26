package com.hj.tj.gohome.vo.requestVo;

import lombok.Data;

/**
 * @author tangj
 * @description
 * @since 2018/10/10 16:13
 */
@Data
public class PortalUserLoginReqObj {
    /**
     * 用户名
     */
    private String account;

    /**
     * 密码
     */
    private String password;

}
