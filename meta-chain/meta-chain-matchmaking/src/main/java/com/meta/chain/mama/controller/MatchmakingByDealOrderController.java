package com.meta.chain.mama.controller;

import com.meta.chain.mama.dto.matchmakingByDealOrder.MatchmakingByDealOrderListDTO;
import com.meta.chain.mama.entity.MatchmakingByDealOrder;
import com.meta.chain.mama.service.IMatchmakingByDealOrderService;
import com.meta.module.common.result.ResponseResult;
import io.swagger.annotations.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 撮合交易订单(被交易方)
 *
 * @author admin
 * @since 2022-02-21
 */
@RestController
@RequestMapping("/matchmaking-by-deal-order")
@Api(tags = "撮合交易订单(被交易方)")
public class MatchmakingByDealOrderController {
    private final IMatchmakingByDealOrderService service;

    public MatchmakingByDealOrderController(IMatchmakingByDealOrderService service) {
        this.service = service;
    }

    @ApiOperation("撮合交易订单(被交易方)列表")
    @PostMapping("/g")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = MatchmakingByDealOrder.class),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "wt", dataType = "String", required = true, value = "token"),
    })
    public ResponseResult lists(@Validated @RequestBody MatchmakingByDealOrderListDTO listDTO) {
        return service.lists(listDTO);
    }
}

