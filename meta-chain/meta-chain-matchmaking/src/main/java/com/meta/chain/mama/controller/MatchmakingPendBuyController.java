package com.meta.chain.mama.controller;

import com.meta.chain.mama.dto.matchmakingPendBuy.MatchmakingPendBuyListDTO;
import com.meta.chain.mama.entity.MatchmakingPendBuy;
import com.meta.chain.mama.service.IMatchmakingPendBuyService;
import com.meta.module.common.result.ResponseResult;
import io.swagger.annotations.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 撮合交易买入挂单
 *
 * @author admin
 * @since 2022-02-21
 */
@RestController
@RequestMapping("/matchmaking-pend-buy")
@Api(tags = "撮合交易买入挂单")
public class MatchmakingPendBuyController {
    private final IMatchmakingPendBuyService service;

    public MatchmakingPendBuyController(IMatchmakingPendBuyService service) {
        this.service = service;
    }

    @ApiOperation("撮合交易买入挂单列表")
    @PostMapping("/g")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = MatchmakingPendBuy.class),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "wt", dataType = "String", required = true, value = "token"),
    })
    public ResponseResult lists(@Validated @RequestBody MatchmakingPendBuyListDTO listDTO) {
        return service.lists(listDTO);
    }
}

