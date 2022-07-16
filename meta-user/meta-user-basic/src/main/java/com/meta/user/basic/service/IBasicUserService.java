package com.meta.user.basic.service;

import com.meta.user.basic.entity.BasicUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meta.user.basic.dto.basicUser.*;
import com.base.result.ResponseResult;
/**
 * 用户基本信息
 *
 * @author admin
 * @since 2022-07-16
 */
public interface IBasicUserService extends IService<BasicUser> {

    ResponseResult lists(BasicUserListDTO listDTO);

    ResponseResult update(BasicUserUpdateDTO updateDTO);

    ResponseResult add(BasicUserAddDTO addDTO);

    ResponseResult del(BasicUserDelDTO delDTO);
}
