package com.hj.tj.gohome.vo.login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WxLoginReqObj {

    /**
     * 微信code
     */
    private String code;

    /**
     * 用户微信数据
     */
    private String encryptedData;

    /**
     * 用户iv
     */
    private String iv;
}
