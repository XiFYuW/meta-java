package com.meta.chain.mama.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchmakingPend implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private String uid;

    @ApiModelProperty(value = "0.撮合中 1.撮合成功 2.撮合取消")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "0.限价 1.市价")
    private Integer mode;

    @ApiModelProperty(value = "挂单数量")
    private BigDecimal amount;

    @ApiModelProperty(value = "成交数量")
    private BigDecimal turnover;

    @ApiModelProperty(value = "成交差值数量")
    private BigDecimal difference;

    @ApiModelProperty(value = "完全成交时间")
    private Date fullTime;

    @ApiModelProperty(value = "交易金额")
    private BigDecimal money;

    @ApiModelProperty(value = "意向价格")
    private BigDecimal intentional;

    @ApiModelProperty(value = "0.未删除 1.已删除")
    private Integer isDel;

    @ApiModelProperty(value = "冻结总金额")
    private BigDecimal freeze;
}
