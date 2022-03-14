package com.meta.chain.mama.struct;

import com.meta.chain.mama.dto.matchmakingByDealOrder.MatchmakingByDealOrderAddDTO;
import com.meta.chain.mama.dto.matchmakingByDealOrder.MatchmakingByDealOrderUpdateDTO;
import com.meta.chain.mama.entity.MatchmakingByDealOrder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MatchmakingByDealOrderStruct {
    MatchmakingByDealOrder toByMatchmakingByDealOrderUpdateDTO(MatchmakingByDealOrderUpdateDTO updateDTO);

    MatchmakingByDealOrder toByMatchmakingByDealOrderAddDTO(MatchmakingByDealOrderAddDTO addDTO);
}
