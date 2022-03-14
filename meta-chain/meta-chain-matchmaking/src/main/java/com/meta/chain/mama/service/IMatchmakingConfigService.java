package com.meta.chain.mama.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meta.chain.mama.dto.matchmakingConfig.MatchmakingConfigAddDTO;
import com.meta.chain.mama.dto.matchmakingConfig.MatchmakingConfigDelDTO;
import com.meta.chain.mama.dto.matchmakingConfig.MatchmakingConfigListDTO;
import com.meta.chain.mama.dto.matchmakingConfig.MatchmakingConfigUpdateDTO;
import com.meta.chain.mama.entity.MatchmakingConfig;
import com.meta.module.common.result.ResponseResult;

/**
 * 撮合交易配置
 *
 * @author admin
 * @since 2022-02-21
 */
public interface IMatchmakingConfigService extends IService<MatchmakingConfig> {

    ResponseResult lists(MatchmakingConfigListDTO listDTO);

    ResponseResult update(MatchmakingConfigUpdateDTO updateDTO);

    ResponseResult add(MatchmakingConfigAddDTO addDTO);

    ResponseResult del(MatchmakingConfigDelDTO delDTO);
}
