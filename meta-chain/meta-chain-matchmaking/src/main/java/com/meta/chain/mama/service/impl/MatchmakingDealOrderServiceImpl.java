package com.meta.chain.mama.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meta.chain.mama.dto.matchmakingDealOrder.MatchmakingDealOrderAddDTO;
import com.meta.chain.mama.dto.matchmakingDealOrder.MatchmakingDealOrderDelDTO;
import com.meta.chain.mama.dto.matchmakingDealOrder.MatchmakingDealOrderListDTO;
import com.meta.chain.mama.dto.matchmakingDealOrder.MatchmakingDealOrderUpdateDTO;
import com.meta.chain.mama.entity.MatchmakingDealOrder;
import com.meta.chain.mama.mapper.MatchmakingDealOrderMapper;
import com.meta.chain.mama.service.IMatchmakingDealOrderService;
import com.meta.chain.mama.struct.MatchmakingDealOrderStruct;
import com.meta.module.common.database.PageUtils;
import com.meta.module.common.exception.HintRuntimeException;
import com.meta.module.common.result.ResponseResult;
import com.meta.module.common.result.ResponseResultUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

/**
 * 撮合交易订单(交易方)
 *
 * @author admin
 * @since 2022-02-21
 */
@Service
@AllArgsConstructor
public class MatchmakingDealOrderServiceImpl extends ServiceImpl<MatchmakingDealOrderMapper, MatchmakingDealOrder> implements IMatchmakingDealOrderService {

    private final MatchmakingDealOrderStruct struct;

    @Override
    @Transactional(readOnly = true)
    public ResponseResult lists(MatchmakingDealOrderListDTO listDTO) {
        final Map<String, Object> data = PageUtils.getDateMap(() -> page(
                PageUtils.getPage(listDTO.getOffset(), listDTO.getLimit()),
                new LambdaQueryWrapper<MatchmakingDealOrder>().orderByDesc(MatchmakingDealOrder::getId))
        );
        return ResponseResultUtils.getResponseResultDataS(data);
    }

    @Override
    @Transactional
    public ResponseResult update(MatchmakingDealOrderUpdateDTO updateDTO) {
        Optional.ofNullable(this.baseMapper.selectById(updateDTO.getId())).orElseThrow(() -> new HintRuntimeException("记录不存在"));
        MatchmakingDealOrder entity = struct.toByMatchmakingDealOrderUpdateDTO(updateDTO);
        this.baseMapper.updateById(entity);
        return ResponseResultUtils.getResponseResultS("修改成功");
    }

    @Override
    @Transactional
    public ResponseResult add(MatchmakingDealOrderAddDTO addDTO) {
        MatchmakingDealOrder entity = struct.toByMatchmakingDealOrderAddDTO(addDTO);
        entity.setCreateTime(DateUtil.date());
        this.baseMapper.insert(entity);
        return ResponseResultUtils.getResponseResultS("添加成功");
    }

    @Override
    @Transactional
    public ResponseResult del(MatchmakingDealOrderDelDTO delDTO) {
        MatchmakingDealOrder entity = Optional.ofNullable(this.baseMapper.selectById(delDTO.getId())).orElseThrow(() -> new HintRuntimeException("记录不存在"));
        this.baseMapper.updateById(entity);
        return ResponseResultUtils.getResponseResultS("删除成功");
    }
}
