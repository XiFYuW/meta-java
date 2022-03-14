package com.meta.chain.mama.controller;

import com.meta.chain.mama.dto.matchmakingConfig.*;
import com.meta.chain.mama.entity.MatchmakingConfig;
import com.meta.chain.mama.service.IMatchmakingConfigService;
import com.meta.module.common.result.ResponseResult;
import io.swagger.annotations.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 撮合交易配置
 *
 * @author admin
 * @since 2022-02-21
 */
@RestController
@RequestMapping("/matchmaking-config")
@Api(tags = "撮合交易配置")
public class MatchmakingConfigController {
    private final IMatchmakingConfigService service;

    public MatchmakingConfigController(IMatchmakingConfigService service) {
        this.service = service;
    }

    @ApiOperation("撮合交易配置列表")
    @PostMapping("/g")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = MatchmakingConfig.class),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "wt", dataType = "String", required = true, value = "token"),
    })
    public ResponseResult lists(@Validated @RequestBody MatchmakingConfigListDTO listDTO) {
        return service.lists(listDTO);
    }

    @ApiOperation("修改撮合交易配置")
    @PostMapping("/u")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "wt", dataType = "String", required = true, value = "token"),
    })
    public ResponseResult update(@Validated @RequestBody MatchmakingConfigUpdateDTO updateDTO) {
        return service.update(updateDTO);
    }

    @ApiOperation("添加撮合交易配置")
    @PostMapping("/a")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "wt", dataType = "String", required = true, value = "token"),
    })
    public ResponseResult add(@Validated @RequestBody MatchmakingConfigAddDTO addDTO) {
        return service.add(addDTO);
    }

    @ApiOperation("删除撮合交易配置")
    @PostMapping("/d")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "wt", dataType = "String", required = true, value = "token"),
    })
    public ResponseResult del(@Validated @RequestBody MatchmakingConfigDelDTO delDTO) {
        return service.del(delDTO);
    }
}

