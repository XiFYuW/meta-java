package com.meta.chain.mama.controller;

import com.meta.chain.mama.dto.matchmakingDealOrder.MatchmakingDealOrderListDTO;
import com.meta.chain.mama.entity.MatchmakingDealOrder;
import com.meta.chain.mama.service.IMatchmakingDealOrderService;
import com.meta.module.common.result.ResponseResult;
import io.swagger.annotations.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 撮合交易订单(交易方)
 *
 * @author admin
 * @since 2022-02-21
 */
@RestController
@RequestMapping("/matchmaking-deal-order")
@Api(tags = "撮合交易订单(交易方)")
public class MatchmakingDealOrderController {
    private final IMatchmakingDealOrderService service;

    public MatchmakingDealOrderController(IMatchmakingDealOrderService service) {
        this.service = service;
    }

    @ApiOperation("撮合交易订单(交易方)列表")
    @PostMapping("/g")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = MatchmakingDealOrder.class),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "wt", dataType = "String", required = true, value = "token"),
    })
    public ResponseResult lists(@Validated @RequestBody MatchmakingDealOrderListDTO listDTO) {
        return service.lists(listDTO);
    }
}

