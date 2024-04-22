package com.xiangjiahui.weblog.admin.config;

import com.xiangjiahui.weblog.jwt.config.JwtAuthenticationSecurityConfig;
import com.xiangjiahui.weblog.jwt.filter.TokenAuthenticationFilter;
import com.xiangjiahui.weblog.jwt.handler.RestAccessDeniedHandler;
import com.xiangjiahui.weblog.jwt.handler.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationSecurityConfig jwtAuthenticationSecurityConfig;

    private final RestAuthenticationEntryPoint authEntryPoint;

    private final RestAccessDeniedHandler deniedHandler;

    public WebSecurityConfig(JwtAuthenticationSecurityConfig jwtAuthenticationSecurityConfig, RestAuthenticationEntryPoint authEntryPoint, RestAccessDeniedHandler deniedHandler) {
        this.jwtAuthenticationSecurityConfig = jwtAuthenticationSecurityConfig;
        this.authEntryPoint = authEntryPoint;
        this.deniedHandler = deniedHandler;
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
                .and().httpBasic().authenticationEntryPoint(authEntryPoint)
                .and().exceptionHandling().accessDeniedHandler(deniedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 前后端分离，无需创建会话
                .and().addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

}
