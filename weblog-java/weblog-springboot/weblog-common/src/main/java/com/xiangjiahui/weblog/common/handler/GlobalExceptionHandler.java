package com.xiangjiahui.weblog.common.handler;


import com.xiangjiahui.weblog.common.utils.HttpUtil;
import com.xiangjiahui.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Response> validException(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder stringBuilder = new StringBuilder();

        Optional.of(bindingResult.getFieldErrors()).ifPresent(errors -> {
            errors.forEach(error -> {
                stringBuilder.append(error.getField()).append(" ")
                        .append(error.getDefaultMessage()).append(", 当前值: '")
                        .append(error.getRejectedValue()).append("': ");
            });
        });

        log.error("{} request error, errorMessage: {} ", HttpUtil.getURL(), stringBuilder);

        return ResponseEntity.badRequest().body(Response.fail(stringBuilder.toString()));
    }
}