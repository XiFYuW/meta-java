package com.meta.chain.mama.struct;

import com.meta.chain.mama.dto.matchmakingConfig.MatchmakingConfigAddDTO;
import com.meta.chain.mama.dto.matchmakingConfig.MatchmakingConfigUpdateDTO;
import com.meta.chain.mama.entity.MatchmakingConfig;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MatchmakingConfigStruct {
    MatchmakingConfig toByMatchmakingConfigUpdateDTO(MatchmakingConfigUpdateDTO updateDTO);

    MatchmakingConfig toByMatchmakingConfigAddDTO(MatchmakingConfigAddDTO addDTO);
}
