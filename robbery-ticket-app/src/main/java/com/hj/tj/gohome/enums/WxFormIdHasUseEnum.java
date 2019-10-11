package com.hj.tj.gohome.enums;

public enum  WxFormIdHasUseEnum {

    NOT_USE(0, "未使用"), USED(1, "已使用");

    private Integer value;
    private String description;

    private WxFormIdHasUseEnum(Integer value, String description) {
        this.description = description;
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
