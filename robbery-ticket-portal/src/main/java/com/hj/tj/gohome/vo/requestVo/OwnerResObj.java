package com.hj.tj.gohome.vo.requestVo;

import lombok.Data;

/**
 * @author tangj
 * @description
 * @since 2018/11/2 9:22
 */
@Data
public class OwnerResObj {

    private Integer id;

    /**
     * 微信号
     */
    private String wxAccount;

    /**
     * 微信昵称
     */
    private String wxNickname;

    /**
     * 业主手机号
     */
    private String phone;

}
