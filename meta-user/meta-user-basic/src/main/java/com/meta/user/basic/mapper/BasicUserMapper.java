package com.meta.user.basic.mapper;

import com.meta.user.basic.entity.BasicUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户基本信息 Mapper 接口
 *
 * @author admin
 * @since 2022-07-16
 */
@Mapper
public interface BasicUserMapper extends BaseMapper<BasicUser> {

}
