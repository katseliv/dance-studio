package com.dataart.dancestudio.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class JwtResponse {

    private final String accessToken;
    private final String refreshToken;

}
