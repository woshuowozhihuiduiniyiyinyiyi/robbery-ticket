package com.hj.tj.gohome.config.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tangj
 * @description 自定义异常处理器
 * @since 2018/11/20 18:13
 */

@Slf4j
@ControllerAdvice
public class MarketExportExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Map<String, Object>> serviceExceptionHandler(ServiceException ex,
                                                                       HttpServletRequest request) {
        Map<String, Object> errorMap = genErrorMap(request.getRequestURI(), ex.getStatus().value(), "自定义异常", ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(errorMap);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> bindConstraintViolationExceptionHandler(ConstraintViolationException ex,
                                                                                       HttpServletRequest request) {
        Map<String, Object> errorMap = genErrorMap(request.getRequestURI(), HttpStatus.BAD_REQUEST.value(), "参数验证异常", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorMap);
    }


    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, Object>> bindExceptionHandler(Exception ex,
                                                                    HttpServletRequest request) {
        List<FieldError> fieldErrors;
        if (ex instanceof BindException) {
            BindException bindException = (BindException) ex;
            fieldErrors = bindException.getFieldErrors();
        } else {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) ex;
            fieldErrors = methodArgumentNotValidException.getBindingResult().getFieldErrors();
        }


        StringBuilder errorBuilder = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            errorBuilder.append(fieldError.getDefaultMessage());
            errorBuilder.append(";");
        }

        Map<String, Object> errorMap = genErrorMap(request.getRequestURI(), HttpStatus.BAD_REQUEST.value(), "参数验证异常", errorBuilder.toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorMap);
    }

    private Map<String, Object> genErrorMap(String path,
                                            int status,
                                            String error,
                                            String message) {
        Map<String, Object> errorMap = new HashMap<>(5);
        errorMap.put("timestamp", new Date());
        errorMap.put("status", status);
        errorMap.put("error", error);
        errorMap.put("message", message);
        errorMap.put("path", path);

        return errorMap;
    }

}
