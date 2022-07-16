package com.meta.user.basic.controller;

import org.springframework.web.bind.annotation.*;
import com.meta.user.basic.entity.BasicUser;
import com.meta.user.basic.service.IBasicUserService;
import com.meta.user.basic.dto.basicUser.*;
import com.base.result.ResponseResult;
import io.swagger.annotations.*;
import org.springframework.validation.annotation.Validated;
import com.base.aop.IsWebLoginAn;
/**
 * 用户基本信息
 *
 * @author admin
 * @since 2022-07-16
 */
@RestController
@RequestMapping("/basic-user")
@Api(tags = "用户基本信息")
public class BasicUserController {
    private final IBasicUserService service;

    public BasicUserController(IBasicUserService service) {
        this.service = service;
    }

    @ApiOperation("用户基本信息列表")
    @GetMapping("/g")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = BasicUser.class),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "wt", dataType = "String", required = true, value = "token"),
    })
    @IsWebLoginAn
    public ResponseResult lists(@Validated @RequestBody BasicUserListDTO listDTO) {
        return service.lists(listDTO);
    }

    @ApiOperation("修改用户基本信息")
    @PostMapping("/u")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "wt", dataType = "String", required = true, value = "token"),
    })
    @IsWebLoginAn
    public ResponseResult update(@Validated @RequestBody BasicUserUpdateDTO updateDTO) {
        return service.update(updateDTO);
    }

    @ApiOperation("添加用户基本信息")
    @PostMapping("/a")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "wt", dataType = "String", required = true, value = "token"),
    })
    @IsWebLoginAn
    public ResponseResult add(@Validated @RequestBody BasicUserAddDTO addDTO) {
        return service.add(addDTO);
    }

    @ApiOperation("删除用户基本信息")
    @DeleteMapping("/d")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "wt", dataType = "String", required = true, value = "token"),
    })
    @IsWebLoginAn
    public ResponseResult del(@Validated @RequestBody BasicUserDelDTO delDTO) {
        return service.del(delDTO);
    }
}

