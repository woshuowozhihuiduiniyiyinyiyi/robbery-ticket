package com.hj.tj.gohome.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum PassengerTypeEnum {

    ADULT(0, "成人"),
    STUDENT(1, "学生"),
    DISABILITY(2, "伤残军人"),
    CHILDREN(3, "儿童");

    private Integer type;
    private String description;

    private PassengerTypeEnum(Integer type, String description) {
        this.type = type;
        this.description = description;
    }

    public static PassengerTypeEnum getPassengerTypeEnumByType(Integer type) {
        if (Objects.isNull(type)) {
            return null;
        }

        for (PassengerTypeEnum passengerTypeEnum : PassengerTypeEnum.values()) {
            if (Objects.equals(passengerTypeEnum.getType(), type)) {
                return passengerTypeEnum;
            }
        }

        return null;
    }

}
