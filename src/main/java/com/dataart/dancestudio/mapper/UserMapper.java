package com.dataart.dancestudio.mapper;

import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;
import com.dataart.dancestudio.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "image", source = "multipartFile.bytes")
    @Mapping(target = "isDeleted", defaultValue = "false")
    @Mapping(target = "timeZone", source = "timeZone", defaultExpression = "java(java.time.ZoneId.systemDefault().toString())")
    UserEntity userDtoToUserEntity(UserDto dto) throws IOException;

    @Mapping(target = "timeZone", ignore = true)
    UserDto userEntityToUserDto(UserEntity entity);

    List<UserViewDto> userEntitiesToUserViewDtoList(Iterable<UserEntity> entities);

}
