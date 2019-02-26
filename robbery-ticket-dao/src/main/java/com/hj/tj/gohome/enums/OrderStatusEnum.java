package com.hj.tj.gohome.enums;

import java.util.Objects;

public enum OrderStatusEnum {

    DELETE(0, "已删除"), ROBBING(1, "抢票中"), SUCCESS(2, "交易成功"), CLOSED(3, "交易关闭");

    private Integer value;
    private String description;

    private OrderStatusEnum(Integer value, String description) {
        this.description = description;
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static OrderStatusEnum getOrderStatusEnumByValue(Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }

        for (OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()) {
            if (Objects.equals(orderStatusEnum.getValue(), value)) {
                return orderStatusEnum;
            }
        }

        return null;
    }
}
