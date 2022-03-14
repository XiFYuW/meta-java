package com.meta.chain.mama.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meta.chain.mama.dto.matchmakingPendSale.MatchmakingPendSaleListDTO;
import com.meta.chain.mama.entity.MatchmakingPend;
import com.meta.chain.mama.entity.MatchmakingPendSale;
import com.meta.module.common.result.ResponseResult;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 撮合交易卖出挂单
 *
 * @author admin
 * @since 2022-02-21
 */
public interface IMatchmakingPendSaleService extends IService<MatchmakingPendSale> {

    ResponseResult lists(MatchmakingPendSaleListDTO listDTO);

    void completely(Date time, MatchmakingPend matchmakingPend);

    void some(Date time, MatchmakingPend matchmakingPend);

    List<MatchmakingPendSale> limited(BigDecimal number, BigDecimal intentional);

    List<MatchmakingPendSale> market(BigDecimal latestPrice);

    BigDecimal meetLimited(BigDecimal number, BigDecimal intentional);

    BigDecimal meetMarket(BigDecimal number, BigDecimal latestPrice);
}
