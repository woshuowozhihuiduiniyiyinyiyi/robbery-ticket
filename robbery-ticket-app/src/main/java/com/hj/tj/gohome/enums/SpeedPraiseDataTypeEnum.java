package com.hj.tj.gohome.enums;

import lombok.Getter;

@Getter
public enum SpeedPraiseDataTypeEnum {

    COMMENT(1, "评论"),
    DYNAMIC(0, "动态");

    private Integer type;
    private String description;

    private SpeedPraiseDataTypeEnum(Integer type, String description) {
        this.description = description;
        this.type = type;
    }
}
