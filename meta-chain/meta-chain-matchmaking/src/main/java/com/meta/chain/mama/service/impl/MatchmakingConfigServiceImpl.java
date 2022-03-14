package com.meta.chain.mama.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meta.chain.mama.dto.matchmakingConfig.MatchmakingConfigAddDTO;
import com.meta.chain.mama.dto.matchmakingConfig.MatchmakingConfigDelDTO;
import com.meta.chain.mama.dto.matchmakingConfig.MatchmakingConfigListDTO;
import com.meta.chain.mama.dto.matchmakingConfig.MatchmakingConfigUpdateDTO;
import com.meta.chain.mama.entity.MatchmakingConfig;
import com.meta.chain.mama.mapper.MatchmakingConfigMapper;
import com.meta.chain.mama.service.IMatchmakingConfigService;
import com.meta.chain.mama.struct.MatchmakingConfigStruct;
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
 * 撮合交易配置
 *
 * @author admin
 * @since 2022-02-21
 */
@Service
@AllArgsConstructor
public class MatchmakingConfigServiceImpl extends ServiceImpl<MatchmakingConfigMapper, MatchmakingConfig> implements IMatchmakingConfigService {

    private final MatchmakingConfigStruct struct;

    @Override
    @Transactional(readOnly = true)
    public ResponseResult lists(MatchmakingConfigListDTO listDTO) {
        final Map<String, Object> data = PageUtils.getDateMap(() -> page(
                PageUtils.getPage(listDTO.getOffset(), listDTO.getLimit()),
                new LambdaQueryWrapper<MatchmakingConfig>().eq(MatchmakingConfig::getIsDel, 0).orderByDesc(MatchmakingConfig::getId))
        );
        return ResponseResultUtils.getResponseResultDataS(data);
    }

    @Override
    @Transactional
    public ResponseResult update(MatchmakingConfigUpdateDTO updateDTO) {
        Optional.ofNullable(this.baseMapper.selectById(updateDTO.getId())).orElseThrow(() -> new HintRuntimeException("记录不存在"));
        MatchmakingConfig entity = struct.toByMatchmakingConfigUpdateDTO(updateDTO);
        entity.setUpdateTime(DateUtil.date());
        this.baseMapper.updateById(entity);
        return ResponseResultUtils.getResponseResultS("修改成功");
    }

    @Override
    @Transactional
    public ResponseResult add(MatchmakingConfigAddDTO addDTO) {
        MatchmakingConfig entity = struct.toByMatchmakingConfigAddDTO(addDTO);
        entity.setCreateTime(DateUtil.date());
        this.baseMapper.insert(entity);
        return ResponseResultUtils.getResponseResultS("添加成功");
    }

    @Override
    @Transactional
    public ResponseResult del(MatchmakingConfigDelDTO delDTO) {
        MatchmakingConfig entity = Optional.ofNullable(this.baseMapper.selectById(delDTO.getId())).orElseThrow(() -> new HintRuntimeException("记录不存在"));
        entity.setIsDel(1);
        entity.setUpdateTime(DateUtil.date());
        this.baseMapper.updateById(entity);
        return ResponseResultUtils.getResponseResultS("删除成功");
    }
}
