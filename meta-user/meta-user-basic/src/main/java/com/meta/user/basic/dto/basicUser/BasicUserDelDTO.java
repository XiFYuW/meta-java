package com.meta.user.basic.dto.basicUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BasicUserDelDTO {

    @ApiModelProperty(value = "id")
    private Long id;
}
