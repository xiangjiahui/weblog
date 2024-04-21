package com.xiangjiahui.weblog.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * PasswordEncoder 接口是 Spring Security 提供的密码加密接口，它定义了密码加密和密码验证的方法。
 * 通过实现这个接口，可以将密码加密为不可逆的哈希值，以及在验证密码时对比哈希值。
 */
@Component
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt 是一种安全且适合密码存储的哈希算法，它在进行哈希时会自动加入“盐”，增加密码的安全性。
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder coder = new BCryptPasswordEncoder();
        System.out.println(coder.encode("xiangjiahui"));
    }
}
