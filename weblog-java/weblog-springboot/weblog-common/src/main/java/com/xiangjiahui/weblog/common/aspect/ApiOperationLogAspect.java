package com.xiangjiahui.weblog.common.aspect;


import com.xiangjiahui.weblog.common.annotation.ApiOperationLog;
import com.xiangjiahui.weblog.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class ApiOperationLogAspect {

    @Pointcut("@annotation(com.xiangjiahui.weblog.common.annotation.ApiOperationLog)")
    public void pointCut(){}


    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable{
        try {
            long startTime = System.currentTimeMillis();

            /**
             * MDC是日志框架中的MDC（Mapped Diagnostic Context）的简称，是SLF4J和log4j1.2.16版本之后引入的。
             * MDC是线程安全的，每个线程都有自己的MDC实例，所以可以在多线程环境下使用。
             * MDC是日志框架提供的一种方案,它允许开发者将一些特定的数据(比如用户ID,请求ID等)存储到当前线程的上下文中,
             * 使得这些数据可以在日志消息中使用。这对于跟踪多线程或高并发应用中的单个请求非常有用。
             * 使用MDC,可以为每个请求分配一个唯一标识ID,并将该标识添加到每条日志消息中,从而方便的区分和跟踪每个请求的日志。
             */
            MDC.put("traceId", UUID.randomUUID().toString());

            //获取请求的类名和方法名
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = joinPoint.getSignature().getName();

            //请求入参
            Object[] params = joinPoint.getArgs();

            //入参转换为json字符串
            String jsonStr  = Arrays.stream(params).map(toJsonStr()).collect(Collectors.joining(", "));


            //功能描述信息
            String description = getApiOperationLogDescription(joinPoint);

            //打印请求相关参数
            log.info("====== 请求开始: [{}], 入参: {}, 请求类: {}, 请求方法: {} ",
                    description, jsonStr, className, methodName);

            //执行切点方法
            Object result = joinPoint.proceed();

            //执行耗时
            long executionTime = System.currentTimeMillis() - startTime;

            // 打印出参等相关信息
            log.info("====== 请求结束: [{}], 耗时: {}ms, 出参: {} ",
                    description, executionTime, JsonUtil.toJsonString(result));
            return result;
        }finally {
            MDC.clear();
        }
    }



    /**
     * 获取注解的描述信息
     * @param joinPoint
     * @return
     */
    private String getApiOperationLogDescription(ProceedingJoinPoint joinPoint) {
        // 1. 从 ProceedingJoinPoint 获取 MethodSignature
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // 2. 使用 MethodSignature 获取当前被注解的 Method
        Method method = signature.getMethod();

        // 3. 从 Method 中提取 LogExecution 注解
        ApiOperationLog apiOperationLog = method.getAnnotation(ApiOperationLog.class);

        // 4. 从 LogExecution 注解中获取 description 属性
        return apiOperationLog.description();
    }

    /**
     * 转 JSON 字符串
     * @return
     */
    private Function<Object, String> toJsonStr() {
        //return arg -> JsonUtil.toJsonString(arg);
        return JsonUtil::toJsonString;
    }
}
