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
* 撮合交易订单(被交易方)
*
* @author admin
* @since 2022-02-21
*/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MatchmakingByDealOrder对象", description="撮合交易订单(被交易方)")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchmakingByDealOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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

    @ApiModelProperty(value = "成交总金额")
    private BigDecimal money;

    @ApiModelProperty(value = "成交手续费")
    private BigDecimal charge;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "交易订单号")
    private String dealOrderNo;

    @ApiModelProperty(value = "挂单id")
    private Long makeId;


}
