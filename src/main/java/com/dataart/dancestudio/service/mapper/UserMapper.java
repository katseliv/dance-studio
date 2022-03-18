package com.dataart.dancestudio.service.mapper;

import com.dataart.dancestudio.db.entity.UserEntity;
import com.dataart.dancestudio.service.model.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.io.IOException;
import java.time.ZoneId;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Named(value = "timeZone")
    default String mapTimeZone(ZoneId zoneId) {
        return zoneId.getId();
    }

    @Mapping(target = "role.id", source = "roleId")
    @Mapping(target = "image", source = "multipartFile.bytes")
    @Mapping(target = "isDeleted", defaultValue = "false")
    @Mapping(target = "timeZone", source = "timeZone", qualifiedByName = "timeZone",
            defaultExpression = "java(java.time.ZoneId.systemDefault().toString())")
    UserEntity toEntity(UserDto dto) throws IOException;

    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "timeZone", ignore = true)
    UserDto fromEntity(UserEntity entity);

    List<UserDto> fromEntities(Iterable<UserEntity> entities);

}
