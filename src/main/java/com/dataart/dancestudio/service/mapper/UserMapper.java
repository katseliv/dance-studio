package com.dataart.dancestudio.service.mapper;

import com.dataart.dancestudio.db.entity.UserEntity;
import com.dataart.dancestudio.service.model.UserDto;
import com.dataart.dancestudio.service.model.view.UserViewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.io.IOException;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "image", source = "multipartFile.bytes")
    @Mapping(target = "isDeleted", defaultValue = "false")
    @Mapping(target = "timeZone", source = "timeZone", qualifiedByName = "timeZone",
            defaultExpression = "java(java.time.ZoneId.systemDefault().toString())")
    UserEntity toEntity(UserDto dto) throws IOException;

    @Named(value = "timeZone")
    default String mapTimeZone(String zoneId) {
        return zoneId;
    }

    @Mapping(target = "timeZone", ignore = true)
    UserDto fromEntity(UserEntity entity);

    List<UserViewDto> fromEntities(Iterable<UserEntity> entities);

}
