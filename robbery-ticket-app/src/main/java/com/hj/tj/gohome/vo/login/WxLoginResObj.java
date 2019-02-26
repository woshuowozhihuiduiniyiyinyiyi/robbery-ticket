package com.hj.tj.gohome.vo.login;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class WxLoginResObj implements Serializable {

    /**
     * 用户id
     */
    private Integer id;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * sid
     */
    private String token;

    /**
     * 头像
     */
        private String avatarUrl;
}
