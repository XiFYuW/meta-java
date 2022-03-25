package com.meta.module.common.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.lang.annotation.Annotation;

@Component
@Aspect
@Order(0)
@Slf4j
public class LogOutInterceptor {

    @Around("@annotation(com.meta.module.common.aop.LogOutAnnotation) && @annotation(logOutAnnotation)")
    public Object sneakLifeLogAround(ProceedingJoinPoint point, LogOutAnnotation logOutAnnotation) throws Throwable{
        Object[] objects = point.getArgs();
        String url = logOutAnnotation.url();
        if (logOutAnnotation.request()) {
            Annotation[][] annotations = JoinPointUtil.getParameterAnnotations(point);
            Object validObject = JoinPointUtil.getParameterAnnotationObject(objects, annotations, Validated.class);
            if (validObject != null) {
                log.warn("请求接口" + url + "请求参数：{}", JSON.toJSONString(validObject));
            }
        } else {
            log.warn("请求接口" + url);
        }
        Object o = point.proceed(objects);
        if (logOutAnnotation.response()) {
            log.warn("返回数据：{}", JSON.toJSONString(o));
        }
        return o;
    }

}
