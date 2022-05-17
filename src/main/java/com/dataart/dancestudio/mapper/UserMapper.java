package com.dataart.dancestudio.mapper;

import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.UserRegistrationDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;
import com.dataart.dancestudio.model.entity.Role;
import com.dataart.dancestudio.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userEntityToUserDto(UserEntity entity);

    @Mapping(target = "image", qualifiedByName = "image")
    UserViewDto userEntityToUserViewDto(UserEntity entity);

    @Named(value = "image")
    default String mapImage(final byte[] image) {
        return Base64.getEncoder().encodeToString(image);
    }

    @Mapping(target = "roles", source = "role", qualifiedByName = "role")
    UserDetailsDto userEntityToUserDetailsDto(UserEntity entity);

    @Named(value = "role")
    default List<Role> mapRoles(final Role role) {
        return List.of(role);
    }

    UserEntity userRegistrationDtoToUserEntityWithPassword(UserRegistrationDto dto, String password) throws IOException;

    @Mapping(target = "image", source = "base64StringImage", qualifiedByName = "base64StringImage")
    void mergeUserEntityAndUserDto(@MappingTarget UserEntity entity, UserDto dto) throws IOException;

    @Named(value = "base64StringImage")
    default byte[] mapBase64StringImage(final String base64StringImage) {
        return Base64.getDecoder().decode(base64StringImage);
    }

    void mergeUserEntityAndUserDtoWithoutPicture(@MappingTarget UserEntity entity, UserDto dto);

    List<UserViewDto> userEntitiesToUserViewDtoList(Iterable<UserEntity> entities);

}
