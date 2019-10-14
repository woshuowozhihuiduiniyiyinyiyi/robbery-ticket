package com.hj.tj.gohome.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum WxTemplateMsgPushTypeEnum {

    SPEED_MESSAGE(0, "你发布的抢票加速动态有一条新的留言，立即查看"),
    SPEED_REPLY(1, "你发布的抢票加速动态有一条新的回复，立即查看"),
    SPEED_PRAISE(2, "你发布的抢票加速动态有一条新的赞，立即查看");

    private Integer value;
    private String description;

    private WxTemplateMsgPushTypeEnum(Integer value, String description) {
        this.description = description;
        this.value = value;
    }

    public static WxTemplateMsgPushTypeEnum getByValue(Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }

        for (WxTemplateMsgPushTypeEnum wxTemplateMsgPushTypeEnum : WxTemplateMsgPushTypeEnum.values()) {
            if (Objects.equals(wxTemplateMsgPushTypeEnum.getValue(), value)) {
                return wxTemplateMsgPushTypeEnum;
            }
        }

        return null;
    }
}
