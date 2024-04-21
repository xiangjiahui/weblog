package com.xiangjiahui.weblog.jwt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 此类是登录的响参类
 * rsp 是 response 的缩写，表示返参，对应的 req 是 request 的缩写，表示请求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRspVO {

    private String token;
}
