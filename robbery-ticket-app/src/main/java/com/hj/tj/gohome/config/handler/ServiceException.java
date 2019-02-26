package com.hj.tj.gohome.config.handler;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @author tangj
 * @description 自定义业务异常类
 * @since 2018/11/20 17:17
 */
@Data
public class ServiceException extends RuntimeException {
    private HttpStatus status;


    public ServiceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }


    public ServiceException(ServiceExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.status = exceptionEnum.getStatus();
    }


    public ServiceException(ServiceExceptionEnum exceptionEnum, Throwable cause) {
        super(exceptionEnum.getMessage(), cause);
        this.status = exceptionEnum.getStatus();
    }

    public ServiceException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }
}
