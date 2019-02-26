package com.hj.tj.gohome.enums;

import lombok.Getter;

@Getter
public enum StatusEnum {

    UN_DELETE(1, "未删除"),
    DELETED(0, "已删除");

    private Integer status;
    private String description;

    private StatusEnum(Integer status, String description) {
        this.description = description;
        this.status = status;
    }
}
