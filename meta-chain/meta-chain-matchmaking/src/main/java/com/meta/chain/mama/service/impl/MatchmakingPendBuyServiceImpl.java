package com.meta.chain.mama.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meta.chain.mama.dto.matchmakingPendBuy.MatchmakingPendBuyListDTO;
import com.meta.chain.mama.entity.MatchmakingPend;
import com.meta.chain.mama.entity.MatchmakingPendBuy;
import com.meta.chain.mama.mapper.MatchmakingPendBuyMapper;
import com.meta.chain.mama.service.IMatchmakingPendBuyService;
import com.meta.module.common.database.PageUtils;
import com.meta.module.common.result.ResponseResult;
import com.meta.module.common.result.ResponseResultUtils;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 撮合交易买入挂单
 *
 * @author admin
 * @since 2022-02-21
 */
@Service
public class MatchmakingPendBuyServiceImpl extends ServiceImpl<MatchmakingPendBuyMapper, MatchmakingPendBuy> implements IMatchmakingPendBuyService {

    private final RLock rLock;
    private final RLock wLock;

    public MatchmakingPendBuyServiceImpl(RedissonClient redissonClient) {
        RReadWriteLock rReadWriteLock = redissonClient.getReadWriteLock(this.getClass().getName());
        this.wLock = rReadWriteLock.writeLock();
        this.rLock = rReadWriteLock.readLock();
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseResult lists(MatchmakingPendBuyListDTO listDTO) {
        final Map<String, Object> data = PageUtils.getDateMap(() -> page(
                PageUtils.getPage(listDTO.getOffset(), listDTO.getLimit()),
                new LambdaQueryWrapper<MatchmakingPendBuy>().eq(MatchmakingPendBuy::getIsDel, false).orderByDesc(MatchmakingPendBuy::getId))
        );
        return ResponseResultUtils.getResponseResultDataS(data);
    }

    @Override
    public void completely(Date time, MatchmakingPend matchmakingPend) {
        wLock.lock();
        try {
            this.update(null, new LambdaUpdateWrapper<MatchmakingPendBuy>()
                    .set(MatchmakingPendBuy::getFullTime, time)
                    .set(MatchmakingPendBuy::getStatus, 1)
                    .set(MatchmakingPendBuy::getTurnover, matchmakingPend.getTurnover())
                    .set(MatchmakingPendBuy::getDifference, matchmakingPend.getDifference())
                    .eq(MatchmakingPendBuy::getId, matchmakingPend.getId())
            );
        }finally {
            wLock.unlock();
        }
    }

    @Override
    public void some(Date time, MatchmakingPend matchmakingPend) {
        wLock.lock();
        try {
            this.update(null, new LambdaUpdateWrapper<MatchmakingPendBuy>()
                    .set(MatchmakingPendBuy::getFullTime, time)
                    .set(MatchmakingPendBuy::getTurnover, matchmakingPend.getTurnover())
                    .set(MatchmakingPendBuy::getDifference, matchmakingPend.getDifference())
                    .eq(MatchmakingPendBuy::getId, matchmakingPend.getId())
            );
        }finally {
            wLock.unlock();
        }
    }

    @Override
    public List<MatchmakingPendBuy> limited(BigDecimal number, BigDecimal intentional) {
        this.rLock.lock();
        try {
            List<MatchmakingPendBuy> data = this.getBaseMapper().selectList(new LambdaQueryWrapper<MatchmakingPendBuy>()
                    .select(MatchmakingPendBuy::getId, MatchmakingPendBuy::getAmount, MatchmakingPendBuy::getTurnover,
                            MatchmakingPendBuy::getUid, MatchmakingPendBuy::getMail, MatchmakingPendBuy::getMobile,
                            MatchmakingPendBuy::getDifference, MatchmakingPendBuy::getIntentional, MatchmakingPendBuy::getFreeze)
                    .eq(MatchmakingPendBuy::getStatus, 0)
                    .eq(MatchmakingPendBuy::getIntentional, intentional)
                    .eq(MatchmakingPendBuy::getAmount, number)
                    .orderByAsc(MatchmakingPendBuy::getId)
                    .last("limit 1")
            );
            if (!CollectionUtils.isEmpty(data)) {
                return data;
            }
            return this.getBaseMapper().selectList(new LambdaQueryWrapper<MatchmakingPendBuy>()
                    .eq(MatchmakingPendBuy::getStatus, 0)
                    .eq(MatchmakingPendBuy::getIntentional, intentional)
                    .orderByAsc(MatchmakingPendBuy::getId)
            );
        } finally {
            this.rLock.unlock();
        }
    }

    @Override
    public List<MatchmakingPendBuy> market(BigDecimal latestPrice) {
        this.rLock.lock();
        try {
            return this.getBaseMapper().selectList(new LambdaQueryWrapper<MatchmakingPendBuy>()
                    .select(MatchmakingPendBuy::getId, MatchmakingPendBuy::getAmount, MatchmakingPendBuy::getTurnover,
                            MatchmakingPendBuy::getUid, MatchmakingPendBuy::getMail, MatchmakingPendBuy::getMobile,
                            MatchmakingPendBuy::getDifference, MatchmakingPendBuy::getIntentional, MatchmakingPendBuy::getFreeze)
                    .eq(MatchmakingPendBuy::getStatus, 0)
                    .le(MatchmakingPendBuy::getIntentional, latestPrice)
                    .orderByDesc(MatchmakingPendBuy::getIntentional)
            );
        }finally {
            this.rLock.unlock();
        }
    }

    @Override
    public BigDecimal meetLimited(BigDecimal number, BigDecimal intentional) {
        this.rLock.lock();
        try {
            BigDecimal amountSum = (BigDecimal) this.getMap(new QueryWrapper<MatchmakingPendBuy>()
                    .select("IFNULL(sum(amount - turnover), 0) as amountSum")
                    .eq("status", 0)
                    .eq("intentional", intentional)
            ).get("amountSum");
            if (amountSum.compareTo(number) < 0) {
                return amountSum;
            }
            return null;
        }finally {
            this.rLock.unlock();
        }
    }

    @Override
    public BigDecimal meetMarket(BigDecimal number, BigDecimal latestPrice) {
        this.rLock.lock();
        try {
            BigDecimal amountSum = (BigDecimal) this.getMap(new QueryWrapper<MatchmakingPendBuy>()
                    .select("IFNULL(sum(amount - turnover), 0) as amountSum")
                    .eq("status", 0)
                    .le("intentional", latestPrice)
            ).get("amountSum");
            if (amountSum.compareTo(number) < 0) {
                return amountSum;
            }
            return null;
        }finally {
            this.rLock.unlock();
        }
    }
}
