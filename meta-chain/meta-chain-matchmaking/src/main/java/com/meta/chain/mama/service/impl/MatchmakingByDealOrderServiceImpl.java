package com.meta.chain.mama.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meta.chain.mama.dto.matchmakingByDealOrder.MatchmakingByDealOrderAddDTO;
import com.meta.chain.mama.dto.matchmakingByDealOrder.MatchmakingByDealOrderDelDTO;
import com.meta.chain.mama.dto.matchmakingByDealOrder.MatchmakingByDealOrderListDTO;
import com.meta.chain.mama.dto.matchmakingByDealOrder.MatchmakingByDealOrderUpdateDTO;
import com.meta.chain.mama.entity.MatchmakingByDealOrder;
import com.meta.chain.mama.mapper.MatchmakingByDealOrderMapper;
import com.meta.chain.mama.service.IMatchmakingByDealOrderService;
import com.meta.chain.mama.struct.MatchmakingByDealOrderStruct;
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
 * 撮合交易订单(被交易方)
 *
 * @author admin
 * @since 2022-02-21
 */
@Service
@AllArgsConstructor
public class MatchmakingByDealOrderServiceImpl extends ServiceImpl<MatchmakingByDealOrderMapper, MatchmakingByDealOrder> implements IMatchmakingByDealOrderService {

    private final MatchmakingByDealOrderStruct struct;

    @Override
    @Transactional(readOnly = true)
    public ResponseResult lists(MatchmakingByDealOrderListDTO listDTO) {
        final Map<String, Object> data = PageUtils.getDateMap(() -> page(
                PageUtils.getPage(listDTO.getOffset(), listDTO.getLimit()),
                new LambdaQueryWrapper<MatchmakingByDealOrder>().orderByDesc(MatchmakingByDealOrder::getId))
        );
        return ResponseResultUtils.getResponseResultDataS(data);
    }

    @Override
    @Transactional
    public ResponseResult update(MatchmakingByDealOrderUpdateDTO updateDTO) {
        Optional.ofNullable(this.baseMapper.selectById(updateDTO.getId())).orElseThrow(() -> new HintRuntimeException("记录不存在"));
        MatchmakingByDealOrder entity = struct.toByMatchmakingByDealOrderUpdateDTO(updateDTO);
        this.baseMapper.updateById(entity);
        return ResponseResultUtils.getResponseResultS("修改成功");
    }

    @Override
    @Transactional
    public ResponseResult add(MatchmakingByDealOrderAddDTO addDTO) {
        MatchmakingByDealOrder entity = struct.toByMatchmakingByDealOrderAddDTO(addDTO);
        entity.setCreateTime(DateUtil.date());
        this.baseMapper.insert(entity);
        return ResponseResultUtils.getResponseResultS("添加成功");
    }

    @Override
    @Transactional
    public ResponseResult del(MatchmakingByDealOrderDelDTO delDTO) {
        MatchmakingByDealOrder entity = Optional.ofNullable(this.baseMapper.selectById(delDTO.getId())).orElseThrow(() -> new HintRuntimeException("记录不存在"));
        this.baseMapper.updateById(entity);
        return ResponseResultUtils.getResponseResultS("删除成功");
    }
}
