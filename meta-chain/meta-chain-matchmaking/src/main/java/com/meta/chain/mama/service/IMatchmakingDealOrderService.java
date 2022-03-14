package com.meta.chain.mama.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meta.chain.mama.dto.matchmakingDealOrder.MatchmakingDealOrderAddDTO;
import com.meta.chain.mama.dto.matchmakingDealOrder.MatchmakingDealOrderDelDTO;
import com.meta.chain.mama.dto.matchmakingDealOrder.MatchmakingDealOrderListDTO;
import com.meta.chain.mama.dto.matchmakingDealOrder.MatchmakingDealOrderUpdateDTO;
import com.meta.chain.mama.entity.MatchmakingDealOrder;
import com.meta.module.common.result.ResponseResult;

/**
 * 撮合交易订单(交易方)
 *
 * @author admin
 * @since 2022-02-21
 */
public interface IMatchmakingDealOrderService extends IService<MatchmakingDealOrder> {

    ResponseResult lists(MatchmakingDealOrderListDTO listDTO);

    ResponseResult update(MatchmakingDealOrderUpdateDTO updateDTO);

    ResponseResult add(MatchmakingDealOrderAddDTO addDTO);

    ResponseResult del(MatchmakingDealOrderDelDTO delDTO);
}
