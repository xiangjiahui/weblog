package com.xiangjiahui.weblog.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    OK(200, "请求成功"),
    FAIL(400, "请求错误"),
    Unauthorized(401, "未授权"),
    Forbidden(403, "禁止访问"),
    NotFound(404, "请求路径不存在"),
    Internal_Server_Error(500, "服务器内部错误"),
    Service_Unavailable(503, "服务不可用"),
    Gateway_Timeout(504, "网关超时"),
    Request_Timeout(408, "请求超时"),
    TO_MANY_REQUESTS(429, "请求过于频繁");

    private final int code;

    private final String message;
}
