package com.xiangjiahui.weblog.jwt.filter;

import com.xiangjiahui.weblog.jwt.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 此过滤器用于校验Token令牌
 * 它继承了 OncePerRequestFilter，确保每个请求只被过滤一次。
 */

@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private  JwtTokenUtil jwtTokenUtil;

    @Autowired
    private  UserDetailsService userDetailsService;

    @Autowired
    private  AuthenticationEntryPoint authenticationEntryPoint;

    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;

    @Value("${jwt.tokenHeaderKey}")
    private String tokenHeaderKey;

//    public TokenAuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService, AuthenticationEntryPoint authenticationEntryPoint) {
//        this.jwtTokenUtil = jwtTokenUtil;
//        this.userDetailsService = userDetailsService;
//        this.authenticationEntryPoint = authenticationEntryPoint;
//    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头中获取 Key 为 Authorization 的 Token 值
        String header = request.getHeader(tokenHeaderKey);

        // 判断value值是否以 Bearer 开头
        if (StringUtils.startsWith(header, tokenPrefix)) {
//            String token = StringUtils.substringAfter(header, "Bearer ");
            String token = StringUtils.substring(header, 7);
            log.info("Token: {}", token);

            // 判空
            if (StringUtils.isNotBlank(token)) {
                try {
                    // 校验Token 是否可用,若解析异常，针对不同异常做出不同的响应参数
                    jwtTokenUtil.validateToken(token);
                } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
                    // 抛出异常，统一让 AuthenticationEntryPoint 处理响应参数
                    authenticationEntryPoint.commence(request, response, new AuthenticationServiceException("Token 不可用"));
                } catch (ExpiredJwtException e) {
                    authenticationEntryPoint.commence(request, response, new AuthenticationServiceException("Token 已过期"));
                    ;
                }
            }

            String username = jwtTokenUtil.getUsernameFromToken(token);

            if (StringUtils.isNotBlank(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                // 根据用户名获取用户详细信息
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 将用户信息存入 authentication, 方便后续校验
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 将authentication 存入 ThreadLocal, 方便后续获取
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 继续执行写一个过滤器
        filterChain.doFilter(request, response);
    }
}
