package com.meta.chain.mama.dto.matchmakingConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MatchmakingConfigDelDTO {

    @ApiModelProperty(value = "id")
    private Integer id;
}
