package com.meta.chain.mama.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meta.chain.mama.dto.matchmakingPendBuy.MatchmakingPendBuyListDTO;
import com.meta.chain.mama.entity.MatchmakingPend;
import com.meta.chain.mama.entity.MatchmakingPendBuy;
import com.meta.module.common.result.ResponseResult;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 撮合交易买入挂单
 *
 * @author admin
 * @since 2022-02-21
 */
public interface IMatchmakingPendBuyService extends IService<MatchmakingPendBuy> {

    ResponseResult lists(MatchmakingPendBuyListDTO listDTO);

    void completely(Date time, MatchmakingPend matchmakingPend);

    void some(Date time, MatchmakingPend matchmakingPend);

    List<MatchmakingPendBuy> limited(BigDecimal number, BigDecimal money);

    List<MatchmakingPendBuy> market(BigDecimal latestPrice);

    BigDecimal meetLimited(BigDecimal number, BigDecimal intentional);

    BigDecimal meetMarket(BigDecimal number, BigDecimal latestPrice);
}
