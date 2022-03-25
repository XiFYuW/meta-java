package com.meta.chain.api.matchmaking.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MatchmakingDealDTO {

    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "用户id不能为空")
    private String uId;

    @ApiModelProperty(value = "意向方式 0.限价 1.市价")
    @Range(message = "mode范围为0到1", max = 1, min = 0)
    private Integer mode;

    @ApiModelProperty(value = "意向行为 0.买入 1.卖出")
    @Range(message = "way范围为0到1", max = 1, min = 0)
    private Integer way;

    @ApiModelProperty(value = "交易对ID")
    @NotNull(message = "交易对ID不能为空")
    private Integer cId;

    @ApiModelProperty(value = "买卖意向价")
    @NotNull(message = "买卖意向价不能为空")
    private BigDecimal money;

    @ApiModelProperty(value = "委托量")
    @NotNull(message = "委托量不能为空")
    private BigDecimal number;

}
