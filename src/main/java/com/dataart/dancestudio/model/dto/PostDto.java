package com.dataart.dancestudio.model.dto;

import com.dataart.dancestudio.annotation.ImageValid;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@JsonDeserialize(builder = PostDto.PostDtoBuilder.class)
public class PostDto {

    @NotNull(message = "User Admin Id is null.")
    @Positive(message = "User Admin Id is negative ot zero.")
    private Integer userAdminId;

    @NotNull(message = "Image is null.")
    @NotBlank(message = "Image is blank.")
    @ImageValid
    private String base64StringImage;

    @NotNull(message = "Text is null.")
    @NotBlank(message = "Text is blank.")
    private String text;

    @JsonPOJOBuilder(withPrefix = "")
    public static class PostDtoBuilder {

    }

}
