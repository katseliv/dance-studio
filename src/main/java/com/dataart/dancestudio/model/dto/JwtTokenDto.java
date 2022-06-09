package com.dataart.dancestudio.model.dto;

import com.dataart.dancestudio.model.JwtTokenType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@JsonDeserialize(builder = JwtTokenDto.JwtTokenDtoBuilder.class)
public class JwtTokenDto {

    private final String token;
    private final JwtTokenType type;
    private final String email;

    @JsonPOJOBuilder(withPrefix = "")
    public static class JwtTokenDtoBuilder {

    }

}
