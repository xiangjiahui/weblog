package com.xiangjiahui.weblog.common.aspect;

import com.xiangjiahui.weblog.common.annotation.ApiRequestLimit;
import com.xiangjiahui.weblog.common.exception.ApiRequestLimitException;
import com.xiangjiahui.weblog.common.utils.HttpUtil;
import com.xiangjiahui.weblog.common.utils.RedisCommand;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Aspect
@Slf4j
@Component
public class ApiRequestLimitAspect {

    @Resource
    private RedisCommand redisCommand;

    @Value("${api.limit}")
    private boolean limit;

    @Pointcut("@annotation(com.xiangjiahui.weblog.common.annotation.ApiRequestLimit)")
    public void pontCut(){}



    @Before(value = "pontCut()")
    public void doBefore(JoinPoint joinPoint) {
        if (!limit){
            return;
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String targetName = joinPoint.getTarget().getClass().getSimpleName();

        Method method = signature.getMethod();
        ApiRequestLimit apiRequestLimit = method.getAnnotation(ApiRequestLimit.class);

        String methodName = method.getName();

        int maxCount = apiRequestLimit.maxCount();

        int seconds = apiRequestLimit.second();

        String description = apiRequestLimit.description();

        String combineKey = getCombineKey(targetName, methodName);

        long currentTime = System.currentTimeMillis();

        redisCommand.zSet(combineKey,currentTime,currentTime);
        redisCommand.expire(combineKey,seconds);
        redisCommand.zRemoveRangeByScore(combineKey,0,currentTime-seconds*1000L);
        Long currentCount = redisCommand.zGet(combineKey);
        if (currentCount > maxCount) {
            log.error("功能: {},[limit]限制请求次数: {}, 当前请求次数: {}, 缓存key: {}",description,maxCount,currentCount,combineKey);
            throw new ApiRequestLimitException("请求次数太频繁了,5秒内只能请求3次!");
        }
    }



    /**
     * 把用户的IP和接口方法名拼接成redis的key
     * @return 组合key
     */
    private String getCombineKey(String targetName,String methodName) {
        return HttpUtil.getRequest().getRemoteAddr() +
                "-" + targetName + "-" + methodName;
    }
}
