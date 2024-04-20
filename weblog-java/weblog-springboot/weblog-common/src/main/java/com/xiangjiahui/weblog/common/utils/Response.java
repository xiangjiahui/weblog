package com.xiangjiahui.weblog.common.utils;


import com.xiangjiahui.weblog.common.enums.ResponseCode;
import lombok.Data;

@Data
public class Response {

    private int code;

    private String message;

    private Object data;

    private boolean success;

    public Response(int code,String message) {
        this.code = code;
        this.message = message;
    }

    public Response(int code, String message, Object data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Response(int code, String message, boolean success){
        this.code = code;
        this.message = message;
        this.success = success;
    }

    public Response(int code, String message, Object data,boolean success){
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
    }


    //-------------成功消息-------------
    public static Response success(){
        return new Response(ResponseCode.OK.getCode(), ResponseCode.OK.getMessage(),true);
    }

    public static Response success(String message){
        return new Response(ResponseCode.OK.getCode(), message,true);
    }

    public static Response success(Object data){
        return new Response(ResponseCode.OK.getCode(), ResponseCode.OK.getMessage(),data,true);
    }

    public static Response success(String message,Object data){
        return new Response(ResponseCode.OK.getCode(), message,data,true);
    }


    //-------------失败消息-------------
    public static Response fail(){
        return new Response(ResponseCode.FAIL.getCode(), ResponseCode.FAIL.getMessage(),false);
    }

    public static Response fail(String message){
        return new Response(ResponseCode.FAIL.getCode(), message,false);
    }

    public static Response fail(Object data){
        return new Response(ResponseCode.FAIL.getCode(), ResponseCode.FAIL.getMessage(),data,false);
    }

    public static Response fail(String message,Object data){
        return new Response(ResponseCode.FAIL.getCode(), message,data,false);
    }


    //-------------未授权消息-------------
    public static Response unauthorized(){
        return new Response(ResponseCode.Unauthorized.getCode(), ResponseCode.Unauthorized.getMessage(),false);
    }

    public static Response unauthorized(String message){
        return new Response(ResponseCode.Unauthorized.getCode(), message,false);
    }

    public static Response unauthorized(Object data){
        return new Response(ResponseCode.Unauthorized.getCode(), ResponseCode.Unauthorized.getMessage(),data,false);
    }

    public static Response unauthorized(String message,Object data){
        return new Response(ResponseCode.Unauthorized.getCode(), message,data,false);
    }


    //-------------禁止访问消息-------------
    public static Response forbidden(){
        return new Response(ResponseCode.Forbidden.getCode(), ResponseCode.Forbidden.getMessage(),false);
    }

    public static Response forbidden(String message){
        return new Response(ResponseCode.Forbidden.getCode(), message,false);
    }

    public static Response forbidden(Object data){
        return new Response(ResponseCode.Forbidden.getCode(), ResponseCode.Forbidden.getMessage(),data,false);
    }

    public static Response forbidden(String message,Object data){
        return new Response(ResponseCode.Forbidden.getCode(), message,data,false);
    }

    //-------------系统内部错误消息-------------
    public static Response internalServerError(){
        return new Response(ResponseCode.Internal_Server_Error.getCode(), ResponseCode.Internal_Server_Error.getMessage(),false);
    }

    public static Response internalServerError(String message){
        return new Response(ResponseCode.Internal_Server_Error.getCode(), message,false);
    }

    public static Response internalServerError(Object data){
        return new Response(ResponseCode.Internal_Server_Error.getCode(), ResponseCode.Internal_Server_Error.getMessage(),data,false);
    }

    public static Response internalServerError(String message,Object data){
        return new Response(ResponseCode.Internal_Server_Error.getCode(), message,data,false);
    }
}
