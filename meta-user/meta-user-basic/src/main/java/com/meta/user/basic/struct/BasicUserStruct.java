package com.meta.user.basic.struct;

import com.meta.user.basic.dto.basicUser.BasicUserAddDTO;
import com.meta.user.basic.dto.basicUser.BasicUserUpdateDTO;
import com.meta.user.basic.entity.BasicUser;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BasicUserStruct {
    BasicUser toByBasicUserUpdateDTO(BasicUserUpdateDTO updateDTO);

    BasicUser toByBasicUserAddDTO(BasicUserAddDTO addDTO);
}
