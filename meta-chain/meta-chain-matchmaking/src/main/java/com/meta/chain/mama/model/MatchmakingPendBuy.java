package com.meta.chain.mama.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
* 撮合交易买入挂单
*
* @author admin
* @since 2022-02-21
*/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MatchmakingPendBuy对象", description="撮合交易买入挂单")
@NoArgsConstructor
@Document(collection="Matchmaking_pend_buy")
public class MatchmakingPendBuy extends MatchmakingPend {

}
