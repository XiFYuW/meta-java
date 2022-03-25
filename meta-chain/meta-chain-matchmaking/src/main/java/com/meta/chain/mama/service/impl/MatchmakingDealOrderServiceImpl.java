package com.meta.chain.mama.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meta.chain.mama.dto.matchmakingDealOrder.MatchmakingDealOrderListDTO;
import com.meta.chain.mama.entity.MatchmakingDealOrder;
import com.meta.chain.mama.mapper.MatchmakingDealOrderMapper;
import com.meta.chain.mama.service.IMatchmakingDealOrderService;
import com.meta.module.common.database.PageUtils;
import com.meta.module.common.result.ResponseResult;
import com.meta.module.common.result.ResponseResultUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 撮合交易订单(交易方)
 *
 * @author admin
 * @since 2022-02-21
 */
@Service
public class MatchmakingDealOrderServiceImpl extends ServiceImpl<MatchmakingDealOrderMapper, MatchmakingDealOrder> implements IMatchmakingDealOrderService {

    @Override
    @Transactional(readOnly = true)
    public ResponseResult lists(MatchmakingDealOrderListDTO listDTO) {
        final Map<String, Object> data = PageUtils.getDateMap(() -> page(
                PageUtils.getPage(listDTO.getOffset(), listDTO.getLimit()),
                new LambdaQueryWrapper<MatchmakingDealOrder>().orderByDesc(MatchmakingDealOrder::getId))
        );
        return ResponseResultUtils.getResponseResultDataS(data);
    }
}
