package com.xiangjiahui.weblog.common.exception;

public class ApiRequestLimitException extends RuntimeException {

    public ApiRequestLimitException() {
    }

    public ApiRequestLimitException(String message) {
        super(message);
    }

    public ApiRequestLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}
