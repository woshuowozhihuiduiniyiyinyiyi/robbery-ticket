package com.hj.tj.gohome.config.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ServiceExceptionEnum {
    /**
     * 系统错误
     */
    SYS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "系统错误"),

    /**
     * 用户错误
     */
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "用户已经存在"),
    USER_NOT_EXISTS(HttpStatus.FORBIDDEN, "用户不存在"),
    USER_LOGIN_ERROR(HttpStatus.FORBIDDEN, "用户名或密码错误"),

    WX_GET_USER_ERROR(HttpStatus.BAD_REQUEST, "微信获取用户信息异常"),

    OWNER_NOT_EXISTS(HttpStatus.BAD_REQUEST, "业主不存在"),

    ORDER_REPEAT(HttpStatus.FORBIDDEN, "请勿在30秒内提交相同订单"),
    ORDER_TIME_LIMIT(HttpStatus.FORBIDDEN, "订单已提交，请勿10秒内重新下单"),

    TICKET_QUERY_ERROR(HttpStatus.FORBIDDEN, "余票信息查询失败,请重新查询"),
    ;

    /**
     * 错误码
     */
    private HttpStatus status;

    /**
     * 错误信息
     */
    private String message;

    private ServiceExceptionEnum(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
