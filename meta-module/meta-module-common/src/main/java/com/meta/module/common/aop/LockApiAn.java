package com.meta.module.common.aop;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface LockApiAn {
    long time() default 3000;

    boolean isSingle() default false;
}
