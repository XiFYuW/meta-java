package com.meta.module.common.database;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
public class BaseDTO implements Serializable {

    @Min(value = 1,message = "页码最少为1")
    @ApiModelProperty(value="页码", example = "1")
    private Integer offset;

    @Min(value = 1,message = "页码大小最少为1")
    @ApiModelProperty(value="页码大小", example = "10")
    private Integer limit;
}
