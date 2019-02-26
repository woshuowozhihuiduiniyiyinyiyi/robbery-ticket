package com.hj.tj.gohome.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum IdCardTypeEnum {

    ID_CARD(0, "身份证"),
    HK_MA_PASSPORT(1, "港澳通行证"),
    TAIWAN_PASSPORT(2, "台湾护照"),
    PASSPORT(3, "护照");

    private Integer idCardType;

    private String description;

    private IdCardTypeEnum(Integer idCardType, String description) {
        this.description = description;
        this.idCardType = idCardType;
    }

    public static IdCardTypeEnum getIdCardTypeEnumByType(Integer idCardType) {
        if (Objects.isNull(idCardType)) {
            return null;
        }

        for (IdCardTypeEnum idCardTypeEnum : IdCardTypeEnum.values()) {
            if (Objects.equals(idCardTypeEnum.getIdCardType(), idCardType)) {
                return idCardTypeEnum;
            }
        }

        return null;
    }
}
