package com.meta.chain.api.matchmaking.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.meta.chain.api.matchmaking.MatchmakingApiService;
import com.meta.chain.api.matchmaking.MatchmakingServer;
import com.meta.chain.api.matchmaking.dto.MatchmakingDealDTO;
import com.meta.chain.mama.entity.MatchmakingConfig;
import com.meta.chain.mama.service.IMatchmakingConfigService;
import com.meta.module.common.exception.HintRuntimeException;
import com.meta.module.common.result.ResponseResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@AllArgsConstructor
public class MatchmakingApiServiceImpl implements MatchmakingApiService {

    private final RedissonClient redissonClient;
    private final MatchmakingServer matchmakingServer;
    private final IMatchmakingConfigService iMatchmakingConfigService;


    @Override
    public ResponseResult deal(MatchmakingDealDTO matchmakingDealDTO) {
        MatchmakingConfig matchmakingConfig = Optional.ofNullable(iMatchmakingConfigService.getOne(new LambdaQueryWrapper<MatchmakingConfig>()
                .eq(MatchmakingConfig::getId, matchmakingDealDTO.getCId()).eq(MatchmakingConfig::getIsDel, 0)
        )).orElseThrow(() -> new HintRuntimeException("配置不存在。"));
        if (matchmakingDealDTO.getMoney().compareTo(matchmakingConfig.getMinMoney()) < 0) {
            throw new HintRuntimeException("买卖最低意向价不能少于" + matchmakingConfig.getMinMoney().toPlainString());
        }
        if (matchmakingDealDTO.getNumber().compareTo(matchmakingConfig.getMinNumber()) < 0) {
            throw new HintRuntimeException("买卖最低委托量不能少于" + matchmakingConfig.getMinNumber().toPlainString());
        }
        if (matchmakingDealDTO.getWay() == 0) {
            return buildBuyLock(matchmakingDealDTO, matchmakingDealDTO.getUId(), matchmakingConfig);
        }
        return buildSaleLock(matchmakingDealDTO, matchmakingDealDTO.getUId(), matchmakingConfig);
    }

    /*买入*/
    public ResponseResult buildBuyLock(MatchmakingDealDTO matchmakingDealDTO, String userId, MatchmakingConfig matchmakingConfig){
        RLock lock = redissonClient.getLock("matchmakingDeal-BUY");
        // 尝试加锁，最多等待30秒，上锁以后10秒自动解锁
        boolean res;
        try {
            res = lock.tryLock(30, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.warn("", e);
            throw new HintRuntimeException("交易失败，请稍后再试。");
        }
        if (res) {
            try {
                return matchmakingServer.buildBuy(matchmakingDealDTO, userId, matchmakingConfig);
            } finally {
                try {
                    lock.unlock();
                }catch (IllegalMonitorStateException e) {
                    log.warn("已自动解锁：", e);
                }
            }
        } else {
            throw new HintRuntimeException("交易失败，请稍后再试。");
        }
    }


    /*卖出*/
    public ResponseResult buildSaleLock(MatchmakingDealDTO matchmakingDealDTO, String userId, MatchmakingConfig matchmakingConfig){
        RLock lock = redissonClient.getLock("matchmakingDeal-SALE");
        // 尝试加锁，最多等待30秒，上锁以后10秒自动解锁
        boolean res;
        try {
            res = lock.tryLock(30, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.warn("", e);
            throw new HintRuntimeException("交易失败，请稍后再试。");
        }
        if (res) {
            try {
                return matchmakingServer.buildSale(matchmakingDealDTO, userId, matchmakingConfig);
            } finally {
                try {
                    lock.unlock();
                }catch (IllegalMonitorStateException e) {
                    log.warn("已自动解锁：", e);
                }
            }
        } else {
            throw new HintRuntimeException("交易失败，请稍后再试。");
        }
    }
}
