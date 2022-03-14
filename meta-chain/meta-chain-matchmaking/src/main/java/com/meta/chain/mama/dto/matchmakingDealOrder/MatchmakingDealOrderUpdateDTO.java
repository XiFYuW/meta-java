package com.meta.chain.mama.dto.matchmakingDealOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MatchmakingDealOrderUpdateDTO {


    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private String uid;

    @ApiModelProperty(value = "邮箱")
    private String mail;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "0.买入 1.卖出")
    private Integer form;

    @ApiModelProperty(value = "总数量")
    private BigDecimal amount;

    @ApiModelProperty(value = "总交易金额")
    private BigDecimal money;

    @ApiModelProperty(value = "总手续费")
    private BigDecimal charge;
}
