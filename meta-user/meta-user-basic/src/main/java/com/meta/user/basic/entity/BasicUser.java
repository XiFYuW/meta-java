package com.meta.user.basic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
/**
* 用户基本信息
*
* @author admin
* @since 2022-07-16
*/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="BasicUser对象", description="用户基本信息")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasicUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
