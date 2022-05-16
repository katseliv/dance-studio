package com.dataart.dancestudio.mapper;

import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.UserRegistrationDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;
import com.dataart.dancestudio.model.entity.Role;
import com.dataart.dancestudio.model.entity.UserEntity;
import org.apache.tomcat.util.codec.binary.Base64;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.io.IOException;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userEntityToUserDto(UserEntity entity);

    @Mapping(target = "image", qualifiedByName = "image")
    UserViewDto userEntityToUserViewDto(UserEntity entity);

    @Named(value = "image")
    default String mapImage(final byte[] image) {
        return Base64.encodeBase64String(image);
    }

    @Mapping(target = "roles", source = "role", qualifiedByName = "role")
    UserDetailsDto userEntityToUserDetailsDto(UserEntity entity);

    @Named(value = "role")
    default List<Role> mapRoles(final Role role) {
        return List.of(role);
    }

    @Mapping(target = "image", source = "dto.multipartFile.bytes")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "isDeleted", defaultValue = "false")
    UserEntity userRegistrationDtoToUserEntityWithPassword(UserRegistrationDto dto, String password) throws IOException;

    @Mapping(target = "image", source = "multipartFile.bytes")
    @Mapping(target = "isDeleted", defaultValue = "false")
    void mergeUserEntityAndUserDto(@MappingTarget UserEntity entity, UserDto dto) throws IOException;

    @Mapping(target = "isDeleted", defaultValue = "false")
    void mergeUserEntityAndUserDtoWithoutPicture(@MappingTarget UserEntity entity, UserDto dto);

    List<UserViewDto> userEntitiesToUserViewDtoList(Iterable<UserEntity> entities);

}
