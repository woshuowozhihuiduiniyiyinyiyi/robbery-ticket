package com.hj.tj.gohome.enums;

import lombok.Getter;

@Getter
public enum WxTemplateMsgHasPushEnum {

    NOT_PUSH(0, "未推送"), PUSHED(1, "已推送");

    private Integer value;
    private String description;

    private WxTemplateMsgHasPushEnum(Integer value, String description) {
        this.description = description;
        this.value = value;
    }
}
