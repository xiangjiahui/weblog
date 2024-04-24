package com.xiangjiahui.weblog.common.annotation;


import java.lang.annotation.*;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
@Documented
public @interface ApiRequestLimit {

    /**
     * Api描述
     * @return
     */
    String description() default "";

    /**
     * 接口最大请求次数
     * @return
     */
    int maxCount() default 3;


    /**
     * 秒
     * @return
     */
    int second() default 5;
}
