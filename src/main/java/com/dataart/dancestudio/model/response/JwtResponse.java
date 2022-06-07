package com.dataart.dancestudio.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class JwtResponse {

    private final String accessToken;
    private final String refreshToken;

}
