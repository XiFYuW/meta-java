package com.meta.chain.mama.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meta.chain.mama.dto.matchmakingByDealOrder.MatchmakingByDealOrderListDTO;
import com.meta.chain.mama.entity.MatchmakingByDealOrder;
import com.meta.module.common.result.ResponseResult;

/**
 * 撮合交易订单(被交易方)
 *
 * @author admin
 * @since 2022-02-21
 */
public interface IMatchmakingByDealOrderService extends IService<MatchmakingByDealOrder> {

    ResponseResult lists(MatchmakingByDealOrderListDTO listDTO);
}
