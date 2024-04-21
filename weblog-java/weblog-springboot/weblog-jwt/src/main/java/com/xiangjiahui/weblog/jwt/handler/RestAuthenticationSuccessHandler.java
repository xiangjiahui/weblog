package com.xiangjiahui.weblog.jwt.handler;

import com.xiangjiahui.weblog.common.utils.Response;
import com.xiangjiahui.weblog.jwt.utils.JwtTokenUtil;
import com.xiangjiahui.weblog.jwt.utils.ResultUtil;
import com.xiangjiahui.weblog.jwt.vo.LoginRspVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 此类实现了 Spring Security 的 AuthenticationSuccessHandler 接口，用于处理身份验证成功后的逻辑。
 * 首先，从 authentication 对象中获取用户的 UserDetails 实例，
 * 这里是主要是获取用户的用户名，然后通过用户名生成 Token 令牌，最后返回数据。
 */

@Component
@Slf4j
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenUtil jwtTokenUtil;

    public RestAuthenticationSuccessHandler(JwtTokenUtil jwtTokenUtil){
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 从 authentication 对象中获取用户的 UserDetails 实例，这里是获取用户的用户名
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String token = jwtTokenUtil.generateToken(username);

        LoginRspVO loginRspVO = new LoginRspVO();
        loginRspVO.setToken(token);

        ResultUtil.ok(response, Response.success(loginRspVO));
    }
}
