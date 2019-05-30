package com.hj.tj.gohome.enums;

import lombok.Getter;

/**
 * @author tangj
 * @description
 * @since 2019/5/30 9:51
 */
@Getter
public enum SpeedDynamicHasTopEnum {

    TOP(1, "已置顶"), UN_TOP(0, "未置顶");

    private Integer value;
    private String description;

    private SpeedDynamicHasTopEnum(Integer value, String description) {
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
