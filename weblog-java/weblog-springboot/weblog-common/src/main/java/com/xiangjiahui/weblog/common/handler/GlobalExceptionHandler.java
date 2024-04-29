package com.xiangjiahui.weblog.common.handler;


import com.xiangjiahui.weblog.common.exception.ApiRequestLimitException;
import com.xiangjiahui.weblog.common.exception.BusinessException;
import com.xiangjiahui.weblog.common.exception.UserNotFoundException;
import com.xiangjiahui.weblog.common.utils.HttpUtil;
import com.xiangjiahui.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;
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
                        .append(error.getRejectedValue()).append("'; ");
            });
        });

        log.error("{} request error, errorMessage: {} ", HttpUtil.getURL(), stringBuilder);

        return ResponseEntity.badRequest().body(Response.fail(stringBuilder.toString()));
    }


    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<Response> accessDeniedException(AccessDeniedException e) {
        // 捕获到鉴权失败异常，主动抛出，交给 RestAccessDeniedHandler 去处理
        log.info("============= 捕获到 AccessDeniedException");
        return ResponseEntity.badRequest().body(Response.fail("没有权限操作此功能"));
    }


    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<Response> findUserException(UserNotFoundException e) {
        log.error("{} request error, errorMessage: {} ", HttpUtil.getURL(), e.getMessage());
        return ResponseEntity.badRequest().body(Response.fail(e.getMessage()));
    }


    @ExceptionHandler(value = ApiRequestLimitException.class)
    public ResponseEntity<Response> apiRequestLimitException(ApiRequestLimitException e) {
        log.error("{} request error, errorMessage: {} ", HttpUtil.getURL(), e.getMessage());
        return ResponseEntity.badRequest().body(Response.fail(e.getMessage()));
    }


    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<Response> businessException(BusinessException e) {
        log.error("{} request error, errorMessage: {} ", HttpUtil.getURL(), e.getMessage());
        return ResponseEntity.badRequest().body(Response.fail(e.getMessage()));
    }


    @ExceptionHandler(value = ConnectException.class)
    public ResponseEntity<Response> connectException(ConnectException e) {
        log.error("{} request error, errorMessage: {} ", HttpUtil.getURL(), e.getMessage());
        return ResponseEntity.badRequest().body(Response.fail("连接失败: " + e.getMessage()));
    }


//    @ExceptionHandler(value = BatchUpdateException.class)
//    public ResponseEntity<Response> batchUpdateException(Exception e) {
//        log.error("{} request error, errorMessage: {} ", HttpUtil.getURL(), e.getMessage());
//        if (e.getMessage().contains("Duplicate entry")) {
//            return ResponseEntity.badRequest().body(Response.fail("该分类已存在"));
//        }
//        return ResponseEntity.badRequest().body(Response.fail(e.getMessage()));
//    }
}
