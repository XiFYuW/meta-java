package com.meta.user.basic.service.impl;

import com.meta.user.basic.entity.BasicUser;
import com.meta.user.basic.mapper.BasicUserMapper;
import com.meta.user.basic.service.IBasicUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meta.user.basic.dto.basicUser.*;
import com.meta.user.basic.struct.BasicUserStruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.base.exception.HintRuntimeException;
import com.base.result.PageUtils;
import com.base.result.ResponseResult;
import com.base.result.ResponseResultUtils;
import java.util.Map;
import java.util.Optional;
/**
 * 用户基本信息
 *
 * @author admin
 * @since 2022-07-16
 */
@Service
public class BasicUserServiceImpl extends ServiceImpl<BasicUserMapper, BasicUser> implements IBasicUserService {

    private final BasicUserStruct struct;

    public BasicUserServiceImpl(BasicUserStruct struct) {
        this.struct = struct;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseResult lists(BasicUserListDTO listDTO) {
        final Map<String, Object> data = PageUtils.getDateMap(() -> page(
                PageUtils.getPage(new Page<>(), listDTO.getOffset(), listDTO.getLimit()),
                new LambdaQueryWrapper<BasicUser>().eq(BasicUser::getIsDel, false).orderByDesc(BasicUser::getId))
        );
        return ResponseResultUtils.getResponseResultDataS(data);
    }

    @Override
    @Transactional
    public ResponseResult update(BasicUserUpdateDTO updateDTO) {
        Optional.ofNullable(this.baseMapper.selectById(updateDTO.getId())).orElseThrow(() -> new HintRuntimeException("记录不存在"));
        BasicUser entity = struct.toByBasicUserUpdateDTO(updateDTO);
        entity.setUpdateTime(DateUtil.date());
        this.baseMapper.updateById(entity);
        return ResponseResultUtils.getResponseResultS("修改成功");
    }

    @Override
    @Transactional
    public ResponseResult add(BasicUserAddDTO addDTO) {
        BasicUser entity = struct.toByBasicUserAddDTO(addDTO);
        entity.setCreateTime(DateUtil.date());
        this.baseMapper.insert(entity);
        return ResponseResultUtils.getResponseResultS("添加成功");
    }

    @Override
    @Transactional
    public ResponseResult del(BasicUserDelDTO delDTO) {
        BasicUser entity = Optional.ofNullable(this.baseMapper.selectById(delDTO.getId())).orElseThrow(() -> new HintRuntimeException("记录不存在"));
        entity.setIsDel(true);
        entity.setUpdateTime(DateUtil.date());
        this.baseMapper.updateById(entity);
        return ResponseResultUtils.getResponseResultS("删除成功");
    }
}
