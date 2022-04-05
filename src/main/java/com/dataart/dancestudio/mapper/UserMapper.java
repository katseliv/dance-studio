package com.dataart.dancestudio.mapper;

import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.UserRegistrationDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;
import com.dataart.dancestudio.model.entity.UserDetailsEntity;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.model.entity.UserRegistrationEntity;
import org.apache.tomcat.util.codec.binary.Base64;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.io.IOException;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "isDeleted", defaultValue = "false")
    @Mapping(target = "image", source = "multipartFile.bytes")
    UserEntity userDtoToUserEntity(UserDto dto) throws IOException;

    @Mapping(target = "isDeleted", defaultValue = "false")
    @Mapping(target = "image", source = "dto.multipartFile.bytes")
    @Mapping(target = "password", source = "password")
    UserRegistrationEntity userRegistrationDtoToUserRegistrationEntityWithPassword(UserRegistrationDto dto, String password) throws IOException;

    UserDetailsDto userDetailsEntityToUserDetailsDto(UserDetailsEntity userDetailsEntity);

    UserDto userEntityToUserDto(UserEntity entity);

    @Mapping(target = "image", qualifiedByName = "image")
    UserViewDto userEntityToUserViewDto(UserEntity entity);

    @Named(value = "image")
    default String mapImage(final byte[] image) {
        return Base64.encodeBase64String(image);
    }

    List<UserViewDto> userEntitiesToUserViewDtoList(Iterable<UserEntity> entities);

}
