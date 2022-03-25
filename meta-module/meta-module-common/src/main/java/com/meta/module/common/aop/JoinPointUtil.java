package com.meta.module.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author https://github.com/XiFYuW
 * @since 2019/12/20 14:47
 */
public class JoinPointUtil {

    private static Method getMethod(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    public static <T extends Annotation> T getAnnotation(JoinPoint joinPoint, Class<T> annotationClass){
        return getMethod(joinPoint).getAnnotation(annotationClass);
    }

    public static Annotation[][] getParameterAnnotations(JoinPoint joinPoint){
        return getMethod(joinPoint).getParameterAnnotations();
    }

    public static Object getParameterAnnotationObject(Object[] params, Annotation[][] annotations, Class clazz){
        for (int i = 0; i < annotations.length; i++) {
            Object param = params[i];
            Annotation[] paramAnn = annotations[i];
            // 参数无注解
            if(param == null || paramAnn.length == 0){
                continue;
            }
            for (Annotation annotation : paramAnn) {
                // 判断当前注解是否为指定注解
                if(annotation.annotationType().equals(clazz)){
                    return param;
                }
            }
        }
        return null;
    }

    public static <T extends Annotation> T getDeclaredAnnotations(JoinPoint joinPoint, Class<T> annotationClass){
        return getMethod(joinPoint).getDeclaredAnnotation(annotationClass);
    }

    @SuppressWarnings("unchecked")
    public static Map<String,Object> proxyFieldByAn(Annotation annotation) throws NoSuchFieldException, IllegalAccessException {
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
        Field field = invocationHandler.getClass().getDeclaredField("memberValues");
        field.setAccessible(true);
        return (Map<String,Object>) field.get(invocationHandler);
    }

    public static Object[] getArgs(JoinPoint joinPoint){
        return joinPoint.getArgs();
    }

    public static String getKey(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = getMethod(joinPoint).getName();
        return className + "." + methodName;
    }
}
