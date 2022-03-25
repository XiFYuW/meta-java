package com.meta.chain.api.matchmaking;

import com.meta.chain.api.matchmaking.dto.MatchmakingDealDTO;
import com.meta.module.common.result.ResponseResult;

public interface MatchmakingApiService {

    ResponseResult deal(MatchmakingDealDTO matchmakingDealDTO);
}
