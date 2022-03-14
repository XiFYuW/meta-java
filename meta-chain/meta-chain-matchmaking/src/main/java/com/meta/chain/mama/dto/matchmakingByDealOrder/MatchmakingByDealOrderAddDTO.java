package com.meta.chain.mama.dto.matchmakingByDealOrder;
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
public class MatchmakingByDealOrderAddDTO {
    @ApiModelProperty(value = "用户ID")
    private String uid;

    @ApiModelProperty(value = "邮箱")
    private String mail;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "成交数量")
    private BigDecimal turnover;

    @ApiModelProperty(value = "成交金额")
    private BigDecimal money;

    @ApiModelProperty(value = "成交手续费")
    private BigDecimal charge;

    @ApiModelProperty(value = "交易订单号")
    private String dealOrderNo;

    @ApiModelProperty(value = "挂单id")
    private Long makeId;

}
