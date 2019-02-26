package com.hj.tj.gohome.enums;

public enum GenderEnum {

    MAN(0, "男"), WOMAN(1, "女");

    private Integer value;
    private String description;

    private GenderEnum(Integer value, String description) {
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
