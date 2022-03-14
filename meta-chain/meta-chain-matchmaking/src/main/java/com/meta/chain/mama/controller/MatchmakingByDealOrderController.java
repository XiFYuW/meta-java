package com.meta.chain.mama.controller;

import com.meta.chain.mama.dto.matchmakingByDealOrder.MatchmakingByDealOrderAddDTO;
import com.meta.chain.mama.dto.matchmakingByDealOrder.MatchmakingByDealOrderDelDTO;
import com.meta.chain.mama.dto.matchmakingByDealOrder.MatchmakingByDealOrderListDTO;
import com.meta.chain.mama.dto.matchmakingByDealOrder.MatchmakingByDealOrderUpdateDTO;
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

    @ApiOperation("修改撮合交易订单(被交易方)")
    @PostMapping("/u")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "wt", dataType = "String", required = true, value = "token"),
    })
    public ResponseResult update(@Validated @RequestBody MatchmakingByDealOrderUpdateDTO updateDTO) {
        return service.update(updateDTO);
    }

    @ApiOperation("添加撮合交易订单(被交易方)")
    @PostMapping("/a")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "wt", dataType = "String", required = true, value = "token"),
    })
    public ResponseResult add(@Validated @RequestBody MatchmakingByDealOrderAddDTO addDTO) {
        return service.add(addDTO);
    }

    @ApiOperation("删除撮合交易订单(被交易方)")
    @PostMapping("/d")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "wt", dataType = "String", required = true, value = "token"),
    })
    public ResponseResult del(@Validated @RequestBody MatchmakingByDealOrderDelDTO delDTO) {
        return service.del(delDTO);
    }
}

