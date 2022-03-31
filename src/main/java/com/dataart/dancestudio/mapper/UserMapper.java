package com.dataart.dancestudio.mapper;

import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;
import com.dataart.dancestudio.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "isDeleted", defaultValue = "false")
    @Mapping(target = "image", source = "multipartFile.bytes")
    UserEntity userDtoToUserEntity(UserDto dto) throws IOException;

    @Mapping(target = "isDeleted", defaultValue = "false")
    @Mapping(target = "image", source = "dto.multipartFile.bytes")
    @Mapping(target = "password", source = "password")
    UserEntity userDtoToUserEntityWithPassword(UserDto dto, String password) throws IOException;

    UserDto userEntityToUserDto(UserEntity entity);

    @Mapping(target = "image", qualifiedByName = "image")
    UserViewDto userEntityToUserViewDto(UserEntity entity);

    @Named(value = "image")
    default String mapImage(final byte[] image) {
        return Base64.getEncoder().encodeToString(image);
    }

    List<UserViewDto> userEntitiesToUserViewDtoList(Iterable<UserEntity> entities);

}
