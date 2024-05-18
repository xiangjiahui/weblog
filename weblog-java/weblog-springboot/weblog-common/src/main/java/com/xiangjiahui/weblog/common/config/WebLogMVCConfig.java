package com.xiangjiahui.weblog.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiangjiahui.weblog.common.utils.HttpUtil;
import com.xiangjiahui.weblog.common.utils.RedisCommand;
import com.xiangjiahui.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@Slf4j
public class WebLogMVCConfig implements WebMvcConfigurer {

    private final RedisCommand redis;

    public WebLogMVCConfig(RedisCommand redis) {
        this.redis = redis;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RedisInterceptor());
    }

    class RedisInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            String url = request.getRequestURI();
            String ip = HttpUtil.getRequest().getRemoteAddr();
            String key = ip + "-" + url;

            Long maxCount = 6L;
            long seconds = 10;

            long current = System.currentTimeMillis();

            try {
                redis.zSet(key,current,current);
                redis.expire(key,seconds);
                redis.zRemoveRangeByScore(key,0,current - seconds * 1000);
                Long count = redis.zGet(key);
                if (count > maxCount) {
                    fail(response);
                    return false;
                }
            }catch (Exception e){
                log.error("redis error: {}",e.getMessage());
            }

            return true;
        }
    }


    public void fail(HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        PrintWriter writer = response.getWriter();

        writer.write(mapper.writeValueAsString(Response.requestTooMany("请求过于频繁,10秒内只能请求6次,请稍后再试")));
        writer.flush();
        writer.close();
    }
}
