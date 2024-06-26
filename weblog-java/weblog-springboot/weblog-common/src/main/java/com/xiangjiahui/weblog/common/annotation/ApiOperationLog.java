package com.xiangjiahui.weblog.common.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface ApiOperationLog {

    /**
     * Api 功能描述
     */
    String description() default "";
}
