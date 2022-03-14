package com.meta.chain.mama.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meta.chain.mama.dto.matchmakingPendSale.MatchmakingPendSaleListDTO;
import com.meta.chain.mama.entity.MatchmakingPend;
import com.meta.chain.mama.entity.MatchmakingPendSale;
import com.meta.chain.mama.mapper.MatchmakingPendSaleMapper;
import com.meta.chain.mama.service.IMatchmakingPendSaleService;
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
 * 撮合交易卖出挂单
 *
 * @author admin
 * @since 2022-02-21
 */
@Service
public class MatchmakingPendSaleServiceImpl extends ServiceImpl<MatchmakingPendSaleMapper, MatchmakingPendSale> implements IMatchmakingPendSaleService {

    private final RLock rLock;
    private final RLock wLock;

    public MatchmakingPendSaleServiceImpl(RedissonClient redissonClient) {
        RReadWriteLock rReadWriteLock = redissonClient.getReadWriteLock(this.getClass().getName());
        this.wLock = rReadWriteLock.writeLock();
        this.rLock = rReadWriteLock.readLock();
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseResult lists(MatchmakingPendSaleListDTO listDTO) {
        final Map<String, Object> data = PageUtils.getDateMap(() -> page(
                PageUtils.getPage(listDTO.getOffset(), listDTO.getLimit()),
                new LambdaQueryWrapper<MatchmakingPendSale>().eq(MatchmakingPendSale::getIsDel, false).orderByDesc(MatchmakingPendSale::getId))
        );
        return ResponseResultUtils.getResponseResultDataS(data);
    }

    @Override
    public void completely(Date time, MatchmakingPend matchmakingPend) {
        wLock.lock();
        try {
            this.update(null, new LambdaUpdateWrapper<MatchmakingPendSale>()
                    .set(MatchmakingPendSale::getFullTime, time)
                    .set(MatchmakingPendSale::getStatus, 1)
                    .set(MatchmakingPendSale::getTurnover, matchmakingPend.getTurnover())
                    .set(MatchmakingPendSale::getDifference, matchmakingPend.getDifference())
                    .eq(MatchmakingPendSale::getId, matchmakingPend.getId())
            );
        }finally {
            wLock.unlock();
        }

    }

    @Override
    public void some(Date time, MatchmakingPend matchmakingPend) {
        wLock.lock();
        try {
            this.update(null, new LambdaUpdateWrapper<MatchmakingPendSale>()
                    .set(MatchmakingPendSale::getFullTime, time)
                    .set(MatchmakingPendSale::getTurnover, matchmakingPend.getTurnover())
                    .set(MatchmakingPendSale::getDifference, matchmakingPend.getDifference())
                    .eq(MatchmakingPendSale::getId, matchmakingPend.getId())
            );
        }finally {
            wLock.unlock();
        }
    }

    @Override
    public List<MatchmakingPendSale> limited(BigDecimal number, BigDecimal intentional) {
        this.rLock.lock();
        try {
            List<MatchmakingPendSale> data = this.getBaseMapper().selectList(new LambdaQueryWrapper<MatchmakingPendSale>()
                    .select(MatchmakingPendSale::getId, MatchmakingPendSale::getAmount, MatchmakingPendSale::getTurnover,
                            MatchmakingPendSale::getUid, MatchmakingPendSale::getMail, MatchmakingPendSale::getMobile,
                            MatchmakingPendSale::getDifference, MatchmakingPendSale::getIntentional, MatchmakingPendSale::getFreeze)
                    .eq(MatchmakingPendSale::getStatus, 0)
                    .eq(MatchmakingPendSale::getIntentional, intentional)
                    .eq(MatchmakingPendSale::getAmount, number)
                    .orderByAsc(MatchmakingPendSale::getId)
                    .last("limit 1")
            );
            if (!CollectionUtils.isEmpty(data)) {
                return data;
            }
            return this.getBaseMapper().selectList(new LambdaQueryWrapper<MatchmakingPendSale>()
                    .eq(MatchmakingPendSale::getStatus, 0)
                    .eq(MatchmakingPendSale::getIntentional, intentional)
                    .orderByAsc(MatchmakingPendSale::getId)
            );
        } finally {
            this.rLock.unlock();
        }
    }

    @Override
    public List<MatchmakingPendSale> market(BigDecimal latestPrice) {
        this.rLock.lock();
        try {
           return this.getBaseMapper().selectList(new LambdaQueryWrapper<MatchmakingPendSale>()
                    .select(MatchmakingPendSale::getId, MatchmakingPendSale::getAmount, MatchmakingPendSale::getTurnover,
                            MatchmakingPendSale::getUid, MatchmakingPendSale::getMail, MatchmakingPendSale::getMobile,
                            MatchmakingPendSale::getDifference, MatchmakingPendSale::getIntentional, MatchmakingPendSale::getFreeze)
                    .eq(MatchmakingPendSale::getStatus, 0)
                    .ge(MatchmakingPendSale::getIntentional, latestPrice)
                    .orderByAsc(MatchmakingPendSale::getIntentional)
            );
        }finally {
            this.rLock.unlock();
        }
    }

    @Override
    public BigDecimal meetLimited(BigDecimal number, BigDecimal intentional) {
        this.rLock.lock();
        try {
            BigDecimal amountSum = (BigDecimal) this.getMap(new QueryWrapper<MatchmakingPendSale>()
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
            BigDecimal amountSum = (BigDecimal) this.getMap(new QueryWrapper<MatchmakingPendSale>()
                    .select("IFNULL(sum(amount - turnover), 0) as amountSum")
                    .eq("status", 0)
                    .ge("intentional", latestPrice)
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
