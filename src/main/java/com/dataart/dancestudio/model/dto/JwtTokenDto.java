package com.dataart.dancestudio.model.dto;

import com.dataart.dancestudio.model.entity.JwtTokenType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class JwtTokenDto {

    private final String token;
    private final JwtTokenType type;
    private final String email;
    private final Boolean isDeleted;

}
