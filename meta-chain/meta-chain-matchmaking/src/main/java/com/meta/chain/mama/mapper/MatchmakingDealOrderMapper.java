package com.meta.chain.mama.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meta.chain.mama.entity.MatchmakingDealOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 撮合交易订单(交易方) Mapper 接口
 *
 * @author admin
 * @since 2022-02-21
 */
@Mapper
public interface MatchmakingDealOrderMapper extends BaseMapper<MatchmakingDealOrder> {

}
