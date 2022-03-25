package com.meta.module.common.aop;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface IpImposeAn {

    long overdue() default 1;

    int count() default 10;

    long lockTime() default 10;
}
