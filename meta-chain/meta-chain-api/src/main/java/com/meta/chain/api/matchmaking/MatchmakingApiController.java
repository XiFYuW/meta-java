package com.meta.chain.api.matchmaking;

import com.meta.chain.api.matchmaking.dto.MatchmakingDealDTO;
import com.meta.module.common.aop.LogOutAnnotation;
import com.meta.module.common.result.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "撮合交易API操作")
@RestController
@RequestMapping("api/mac")
public class MatchmakingApiController {

    private final MatchmakingApiService matchmakingApiService;

    public MatchmakingApiController(MatchmakingApiService matchmakingApiService) {
        this.matchmakingApiService = matchmakingApiService;
    }

    @ApiOperation(value = "交易")
    @PostMapping("/dt")
    @LogOutAnnotation(url = "/api/mac/dt")
    public ResponseResult deal(@Validated @RequestBody MatchmakingDealDTO matchmakingDealDTO){
        return matchmakingApiService.deal(matchmakingDealDTO);
    }

}
