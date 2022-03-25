package com.meta.chain.mama.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
* 撮合交易订单(交易方)
*
* @author admin
* @since 2022-02-21
*/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MatchmakingDealOrder对象", description="撮合交易订单(交易方)")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchmakingDealOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private String uid;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "0.买入 1.卖出")
    private Integer form;

    @ApiModelProperty(value = "成交数量")
    private BigDecimal turnover;

    @ApiModelProperty(value = "成交总金额")
    private BigDecimal money;

    @ApiModelProperty(value = "成交手续费")
    private BigDecimal charge;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;


}
