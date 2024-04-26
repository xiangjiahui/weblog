package com.xiangjiahui.weblog.common.utils;


import com.xiangjiahui.weblog.common.enums.ResponseCode;
import lombok.Data;

@Data
public class Response {

    private int code;

    private String message;

    private Object data;

    private boolean success;

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Response(boolean success,int code, String message) {
        this.code = code;
        this.message = message;
        this.success = success;
    }

    public Response(boolean success,int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
    }


    //-------------成功消息-------------
    public static Response success() {
        return new Response(true,ResponseCode.OK.getCode(),ResponseCode.OK.getMessage());
    }

    public static Response success(String message) {
        return new Response(true,ResponseCode.OK.getCode(),message);
    }

    public static Response success(Object data) {
        return new Response(true,ResponseCode.OK.getCode(),ResponseCode.OK.getMessage(),data);
    }

    public static Response success(String message, Object data) {
        return new Response(true,ResponseCode.OK.getCode(),message,data);
    }


    //-------------失败消息-------------
    public static Response fail() {
        return new Response(false,ResponseCode.FAIL.getCode(), ResponseCode.FAIL.getMessage());
    }

    public static Response fail(String message) {
        return new Response(false,ResponseCode.FAIL.getCode(), message);
    }

    public static Response fail(Object data) {
        return new Response(false,ResponseCode.FAIL.getCode(), ResponseCode.FAIL.getMessage(), data);
    }

    public static Response fail(String message, Object data) {
        return new Response(false,ResponseCode.FAIL.getCode(), message, data);
    }


    //-------------未授权消息-------------
    public static Response unauthorized() {
        return new Response(false,ResponseCode.Unauthorized.getCode(), ResponseCode.Unauthorized.getMessage());
    }

    public static Response unauthorized(String message) {
        return new Response(false,ResponseCode.Unauthorized.getCode(), message);
    }

    public static Response unauthorized(Object data) {
        return new Response(false,ResponseCode.Unauthorized.getCode(), ResponseCode.Unauthorized.getMessage(), data);
    }

    public static Response unauthorized(String message, Object data) {
        return new Response(false,ResponseCode.Unauthorized.getCode(), message, data);
    }


    //-------------禁止访问消息-------------
    public static Response forbidden() {
        return new Response(false,ResponseCode.Forbidden.getCode(), ResponseCode.Forbidden.getMessage());
    }

    public static Response forbidden(String message) {
        return new Response(false,ResponseCode.Forbidden.getCode(), message);
    }

    public static Response forbidden(Object data) {
        return new Response(false,ResponseCode.Forbidden.getCode(), ResponseCode.Forbidden.getMessage(), data);
    }

    public static Response forbidden(String message, Object data) {
        return new Response(false,ResponseCode.Forbidden.getCode(), message, data);
    }

    //-------------系统内部错误消息-------------
    public static Response internalServerError() {
        return new Response(false,ResponseCode.Internal_Server_Error.getCode(), ResponseCode.Internal_Server_Error.getMessage());
    }

    public static Response internalServerError(String message) {
        return new Response(false,ResponseCode.Internal_Server_Error.getCode(), message);
    }

    public static Response internalServerError(Object data) {
        return new Response(false,ResponseCode.Internal_Server_Error.getCode(), ResponseCode.Internal_Server_Error.getMessage(), data);
    }

    public static Response internalServerError(String message, Object data) {
        return new Response(false,ResponseCode.Internal_Server_Error.getCode(), message, data);
    }
}
