package com.xiangjiahui.weblog.admin.config;

import com.xiangjiahui.weblog.jwt.config.JwtAuthenticationSecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationSecurityConfig jwtAuthenticationSecurityConfig;

    public WebSecurityConfig(JwtAuthenticationSecurityConfig jwtAuthenticationSecurityConfig) {
        this.jwtAuthenticationSecurityConfig = jwtAuthenticationSecurityConfig;
    }

    @Override
    protected void configure(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
        /*http.authorizeHttpRequests()
                .mvcMatchers("/admin/**").authenticated() //认证所有以admin为前缀的URL资源
                .anyRequest().permitAll().and() //  其它的放行无需认证
                .formLogin().and()  //  使用表单登录
                .httpBasic();   //  使用HTTP Basic认证*/
        http.csrf().disable(). // 禁用 csrf，前后端分离不需要启用CSRF 防护
                formLogin().disable() // 禁用表单登录
                .apply(jwtAuthenticationSecurityConfig) // 设置用户登录认证相关配置
                .and()
                .authorizeHttpRequests()
                .mvcMatchers("/admin/**").authenticated() // 认证所有以 /admin 为前缀的 URL 资源
                .anyRequest().permitAll() // 其他都需要放行，无需认证
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 前后端分离，无需创建会话
    }
}
