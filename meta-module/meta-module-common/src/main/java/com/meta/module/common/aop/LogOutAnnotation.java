package com.meta.module.common.aop;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface LogOutAnnotation {
    String url() default "";

    boolean request() default true;

    boolean response() default true;
}
