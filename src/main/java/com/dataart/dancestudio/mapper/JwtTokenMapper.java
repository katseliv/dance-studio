package com.dataart.dancestudio.mapper;

import com.dataart.dancestudio.model.dto.JwtTokenDto;
import com.dataart.dancestudio.model.entity.JwtTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface JwtTokenMapper {

    JwtTokenEntity jwtTokenDtoToJwtTokenEntity(JwtTokenDto dto);

    void mergeJwtTokenEntityAndJwtTokenDto(@MappingTarget JwtTokenEntity entity, JwtTokenDto dto);

}
