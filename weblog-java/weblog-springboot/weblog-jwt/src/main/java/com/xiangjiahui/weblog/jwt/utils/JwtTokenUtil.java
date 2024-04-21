package com.xiangjiahui.weblog.jwt.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;

/**
 * Jwt工具类,封装所有Jwt相关功能
 */
@Component
@Slf4j
public class JwtTokenUtil implements InitializingBean {

    /**
     * 签发人
     */
    @Value("${jwt.issuer}")
    private String issuer;

    /**
     * 密钥
     */
    private Key key;

    /**
     * Jwt解析
     */
    private JwtParser jwtParser;


    /**
     * 解码配置文件中的配置的Base64编码的 Key 为密钥
     */
    @Value("${jwt.secret}")
    public void setBase64Key(String base64Key){
        key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64Key));
    }


    /**
     * 初始化JwtParser
     */
    @Override
    public void afterPropertiesSet() throws Exception{
        // 考虑到不同服务器之间可能存在时钟偏移，setAllowedClockSkewSeconds 用于设置能够容忍的最大的时钟误差
        jwtParser = Jwts.parserBuilder().requireIssuer(issuer)
                .setSigningKey(key).setAllowedClockSkewSeconds(10)
                .build();
    }


    /**
     * 生成并初始化Token
     */
    public String generateToken(String username){
        LocalDateTime now = LocalDateTime.now();

        // Token 1 小时后失效
        LocalDateTime expireTime = now.plusHours(1);

        return Jwts.builder().setSubject(username).setIssuer(issuer)
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(key)
                .compact();
    }


    /**
     * 解析Token
     */
    public Jws<Claims> parseToken(String token){
        try {
            return jwtParser.parseClaimsJws(token);
        }catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new BadCredentialsException("Token 不可用");
        }catch (ExpiredJwtException e){
            throw new CredentialsExpiredException("Token 失效");
        }
    }


    /**
     * 生成一个Base64 的安全密钥
     */
    public static String generateBase64SecretKey(){
        // 生成安全密钥
        Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        // 将密钥转换为 Base64 编码
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }


    public static void main(String[] args) {
        String key = generateBase64SecretKey();
        log.info("======================密钥Key: {} ", key);
    }
}
