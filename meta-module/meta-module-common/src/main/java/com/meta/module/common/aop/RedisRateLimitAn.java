package com.meta.module.common.aop;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RedisRateLimitAn {
    /**
     * 限流唯一标示
     */
    String key();

    /**
     * 限流时间
     */
    long time() default 1;

    /**
     * 限流次数
     */
    long count() default 10;
}
