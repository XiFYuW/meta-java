package com.meta.chain.mama.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meta.chain.mama.dto.matchmakingByDealOrder.MatchmakingByDealOrderListDTO;
import com.meta.chain.mama.entity.MatchmakingByDealOrder;
import com.meta.chain.mama.mapper.MatchmakingByDealOrderMapper;
import com.meta.chain.mama.service.IMatchmakingByDealOrderService;
import com.meta.module.common.database.PageUtils;
import com.meta.module.common.result.ResponseResult;
import com.meta.module.common.result.ResponseResultUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 撮合交易订单(被交易方)
 *
 * @author admin
 * @since 2022-02-21
 */
@Service
public class MatchmakingByDealOrderServiceImpl extends ServiceImpl<MatchmakingByDealOrderMapper, MatchmakingByDealOrder> implements IMatchmakingByDealOrderService {


    @Override
    @Transactional(readOnly = true)
    public ResponseResult lists(MatchmakingByDealOrderListDTO listDTO) {
        final Map<String, Object> data = PageUtils.getDateMap(() -> page(
                PageUtils.getPage(listDTO.getOffset(), listDTO.getLimit()),
                new LambdaQueryWrapper<MatchmakingByDealOrder>().orderByDesc(MatchmakingByDealOrder::getId))
        );
        return ResponseResultUtils.getResponseResultDataS(data);
    }
}
