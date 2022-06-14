package com.dataart.dancestudio.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@JsonDeserialize(builder = LikeDto.LikeDtoBuilder.class)
public class LikeDto {

    @NotNull(message = "User Id is null.")
    @Positive(message = "User Id is negative ot zero.")
    private Integer userId;

    @JsonPOJOBuilder(withPrefix = "")
    public static class LikeDtoBuilder {

    }

}
