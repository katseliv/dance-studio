package com.dataart.dancestudio.mapper;

import com.dataart.dancestudio.model.dto.PostDto;
import com.dataart.dancestudio.model.dto.view.PostViewDto;
import com.dataart.dancestudio.model.entity.PostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Base64;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "authorFirstName", source = "userAdmin.firstName")
    @Mapping(target = "authorLastName", source = "userAdmin.lastName")
    @Mapping(target = "base64StringImage", source = "image", qualifiedByName = "bytesArrayImage")
    PostViewDto postEntityToPostViewDto(PostEntity entity);

    @Mapping(target = "userAdmin.id", source = "userAdminId")
    @Mapping(target = "image", source = "base64StringImage", qualifiedByName = "base64StringImage")
    PostEntity postDtoToPostEntity(PostDto dto);

    @Named(value = "bytesArrayImage")
    default String mapBytesArrayImage(final byte[] image) {
        return Base64.getEncoder().encodeToString(image);
    }

    @Named(value = "base64StringImage")
    default byte[] mapBase64StringImage(final String base64StringImage) {
        return Base64.getDecoder().decode(base64StringImage);
    }

    List<PostViewDto> postEntitiesToPostViewDtoList(Iterable<PostEntity> entities);

}
