package com.dataart.dancestudio.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class LoginResponse {

    private final String jwtToken;
    private final String refreshToken;
    private final String expiryDuration;

}
