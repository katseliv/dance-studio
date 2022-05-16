package com.dataart.dancestudio.mapper;

import com.dataart.dancestudio.model.dto.JwtTokenDto;
import com.dataart.dancestudio.model.entity.JwtTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface JwtTokenMapper {

    @Mapping(target = "isDeleted", defaultValue = "false")
    JwtTokenEntity jwtTokenDtoToJwtTokenEntity(JwtTokenDto dto);

    @Mapping(target = "isDeleted", defaultValue = "false")
    void mergeJwtTokenEntityAndJwtTokenDto(@MappingTarget JwtTokenEntity entity, JwtTokenDto dto);

}
