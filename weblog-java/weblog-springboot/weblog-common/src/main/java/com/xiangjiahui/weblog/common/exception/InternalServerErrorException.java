package com.xiangjiahui.weblog.common.exception;

public class InternalServerErrorException extends RuntimeException{

    public InternalServerErrorException(){
        super();
    }

    public InternalServerErrorException(String message){
        super(message);
    }

    public InternalServerErrorException(String message,Throwable cause){
        super(message,cause);
    }
}
