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
* 撮合交易配置
*
* @author admin
* @since 2022-02-21
*/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MatchmakingConfig对象", description="撮合交易配置")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchmakingConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "别名(卖出对名称/买入对名称)")
    private String alias;

    @ApiModelProperty(value = "卖出对")
    private Integer sale;

    @ApiModelProperty(value = "卖出对名称")
    private String saleName;

    @ApiModelProperty(value = "卖出对包指令")
    private String saleInstruct;

    @ApiModelProperty(value = "买入对")
    private Integer buy;

    @ApiModelProperty(value = "买入对名称")
    private String buyName;

    @ApiModelProperty(value = "买入对包指令")
    private String buyInstruct;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "0.未删除 1.已删除")
    private Integer isDel;

    @ApiModelProperty(value = "卖出手续费")
    private BigDecimal saleCharge;

    @ApiModelProperty(value = "卖出扣手续费方式 0.数量 1.比例")
    private Integer saleDebit;

    @ApiModelProperty(value = "买入手续费")
    private BigDecimal buyCharge;

    @ApiModelProperty(value = "买入扣手续费方式 0.数量 1.比例")
    private Integer buyDebit;

    @ApiModelProperty(value = "买卖最低意向价")
    private BigDecimal minMoney;

    @ApiModelProperty(value = "买卖最低委托量")
    private BigDecimal minNumber;

    @ApiModelProperty(value = "初始价格")
    private BigDecimal initPrice;


}
