package com.meta.chain.mama.struct;

import com.meta.chain.mama.dto.matchmakingDealOrder.MatchmakingDealOrderAddDTO;
import com.meta.chain.mama.dto.matchmakingDealOrder.MatchmakingDealOrderUpdateDTO;
import com.meta.chain.mama.entity.MatchmakingDealOrder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MatchmakingDealOrderStruct {
    MatchmakingDealOrder toByMatchmakingDealOrderUpdateDTO(MatchmakingDealOrderUpdateDTO updateDTO);

    MatchmakingDealOrder toByMatchmakingDealOrderAddDTO(MatchmakingDealOrderAddDTO addDTO);
}
