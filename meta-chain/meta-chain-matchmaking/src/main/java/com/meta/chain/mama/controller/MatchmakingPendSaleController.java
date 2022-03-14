package com.meta.chain.mama.controller;

import com.meta.chain.mama.dto.matchmakingPendSale.MatchmakingPendSaleListDTO;
import com.meta.chain.mama.entity.MatchmakingPendSale;
import com.meta.chain.mama.service.IMatchmakingPendSaleService;
import com.meta.module.common.result.ResponseResult;
import io.swagger.annotations.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 撮合交易卖出挂单
 *
 * @author admin
 * @since 2022-02-21
 */
@RestController
@RequestMapping("/matchmaking-pend-sale")
@Api(tags = "撮合交易卖出挂单")
public class MatchmakingPendSaleController {
    private final IMatchmakingPendSaleService service;

    public MatchmakingPendSaleController(IMatchmakingPendSaleService service) {
        this.service = service;
    }

    @ApiOperation("撮合交易卖出挂单列表")
    @PostMapping("/ps")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = MatchmakingPendSale.class),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "wt", dataType = "String", required = true, value = "token"),
    })
    public ResponseResult lists(@Validated @RequestBody MatchmakingPendSaleListDTO listDTO) {
        return service.lists(listDTO);
    }

}

