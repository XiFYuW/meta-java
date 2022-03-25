package com.meta.module.common.aop;

import com.meta.module.common.exception.HintRuntimeException;
import com.meta.module.common.ip.IPUtils;
import com.meta.module.common.req.RequestHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.redisson.api.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Order(0)
@Slf4j
public class RedisRateLimitAspect {

    private final RedissonClient redissonClient;

    public RedisRateLimitAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Before(value = "@annotation(com.meta.module.common.aop.RedisRateLimitAn) && @annotation(redisRateLimitAn)")
    public void redisRateLimitAspectBefore(JoinPoint point, RedisRateLimitAn redisRateLimitAn) {
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        String ip = IPUtils.getIp(request);
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(RedisRateLimitAspect.class.getName() + ip + ":" + redisRateLimitAn.key());
        if (rateLimiter.isExists()) {
            /*读取已经存在配置*/
            RateLimiterConfig rateLimiterConfig = rateLimiter.getConfig();
            long rateInterval = rateLimiterConfig.getRateInterval();
            long rate = rateLimiterConfig.getRate();
            if (redisRateLimitAn.time() != rateInterval || rate != redisRateLimitAn.count()) {
                /*移除配置，重新加载配置*/
                rateLimiter.delete();
                rateLimiter.trySetRate(RateType.OVERALL, redisRateLimitAn.count(), redisRateLimitAn.time(), RateIntervalUnit.SECONDS);
            }
        } else {
            rateLimiter.trySetRate(RateType.OVERALL, redisRateLimitAn.count(), redisRateLimitAn.time(), RateIntervalUnit.SECONDS);
        }
        boolean flag = rateLimiter.tryAcquire();
        if (!flag) {
            throw new HintRuntimeException("当前访问频率过高，请稍后再试。");
        }
    }
}
