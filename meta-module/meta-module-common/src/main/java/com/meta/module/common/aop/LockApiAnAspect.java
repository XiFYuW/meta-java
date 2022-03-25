package com.meta.module.common.aop;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.alibaba.fastjson.JSON;
import com.meta.module.common.exception.HintRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author https://github.com/XiFYuW
 * @since  2020/10/21 10:38
 */
@Component
@Aspect
@Order(2)
@Slf4j
public class LockApiAnAspect {

    private final RedisTemplate<String, Object> redisTemplate;
    private final static String LOCK_API_KEY = "LOCK_API_KEY:";
    private final Digester md5;

    public LockApiAnAspect(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.md5 = new Digester(DigestAlgorithm.MD5);
    }

    @Around("@annotation(com.meta.module.common.aop.LockApiAn) && @annotation(lockApiAn)")
    public Object lockApiAnAspectAround(ProceedingJoinPoint point, LockApiAn lockApiAn) throws Throwable {
        Object[] objects = point.getArgs();
        String value = JSON.toJSONString(objects[0]);
        String key = md5.digestHex(value);
        boolean isLock = redisTemplate.opsForValue().setIfAbsent(LOCK_API_KEY + key, value, lockApiAn.time(), TimeUnit.MILLISECONDS);
        if (!isLock) {
            throw new HintRuntimeException("请勿频繁操作");
        }
        try {
            return point.proceed(point.getArgs());
        }finally {
            if (!lockApiAn.isSingle()) {
                redisTemplate.delete(LOCK_API_KEY + value);
            }
        }
    }
}
