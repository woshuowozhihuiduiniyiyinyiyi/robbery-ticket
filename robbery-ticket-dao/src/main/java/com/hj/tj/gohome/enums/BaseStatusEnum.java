package com.hj.tj.gohome.enums;

public enum BaseStatusEnum {

    DELETE(0, "已删除"), UN_DELETE(1, "未删除");

    private Integer value;
    private String description;

    private BaseStatusEnum(Integer value, String description) {
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
