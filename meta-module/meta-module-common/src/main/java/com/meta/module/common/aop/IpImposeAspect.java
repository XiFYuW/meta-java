package com.meta.module.common.aop;

import com.meta.module.common.exception.HintRuntimeException;
import com.meta.module.common.ip.IPUtils;
import com.meta.module.common.req.RequestHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
@Order(0)
@Slf4j
public class IpImposeAspect {

    private final RedisTemplate<String, Object> redisTemplate;
    private final static String ipLockKey = "ipLockKey:";
    private final static String ipLockKeySet = "ipLockKeySet:";

    public IpImposeAspect(RedisTemplate<String, Object> redisTemplater) {
        this.redisTemplate = redisTemplater;
    }

    @Before("@annotation(com.meta.module.common.aop.IpImposeAn) && @annotation(ipImposeAn)")
    public void isLoginAspectBefore(JoinPoint point, IpImposeAn ipImposeAn) {
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        String ip = IPUtils.getIp(request);
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Boolean isIpLockKeySetKey = redisTemplate.hasKey(ipLockKeySet + ip);
        if (isIpLockKeySetKey != null && isIpLockKeySetKey){
            throw new HintRuntimeException("请稍后再试访问");
        }
        String ipKey = ipLockKey + ip + ":" + JoinPointUtil.getKey(point);
        Object count = valueOperations.get(ipKey);
        if (count != null) {
            Integer c = (Integer) count;
            if (c.compareTo(ipImposeAn.count()) > 0) {
                valueOperations.set(ipLockKeySet + ip, ip, ipImposeAn.lockTime(), TimeUnit.SECONDS);
                throw new HintRuntimeException("请勿频繁操作！");
            }
        }
        Boolean isHasKey = redisTemplate.hasKey(ipKey);
        if (isHasKey != null && isHasKey) {
            redisTemplate.opsForValue().increment(ipKey);
        } else {
            valueOperations.set(ipKey, 1, ipImposeAn.overdue(), TimeUnit.SECONDS);
        }
    }
}
