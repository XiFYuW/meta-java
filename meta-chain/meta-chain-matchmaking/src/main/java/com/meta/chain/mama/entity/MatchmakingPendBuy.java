package com.meta.chain.mama.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
* 撮合交易买入挂单
*
* @author admin
* @since 2022-02-21
*/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MatchmakingPendBuy对象", description="撮合交易买入挂单")
//@Builder
@NoArgsConstructor
//@AllArgsConstructor
public class MatchmakingPendBuy extends MatchmakingPend {

}
