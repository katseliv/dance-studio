package com.dataart.dancestudio.mapper;

import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "image", source = "multipartFile.bytes")
    @Mapping(target = "isDeleted", defaultValue = "false")
    @Mapping(target = "timeZone", source = "timeZone", defaultExpression = "java(java.time.ZoneId.systemDefault().toString())")
    UserEntity toEntity(UserDto dto) throws IOException;

    @Mapping(target = "timeZone", ignore = true)
    UserDto fromEntity(UserEntity entity);

    List<UserViewDto> fromEntities(Iterable<UserEntity> entities);

}
