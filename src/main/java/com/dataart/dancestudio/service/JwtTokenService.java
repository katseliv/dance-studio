package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.JwtTokenDto;
import com.dataart.dancestudio.model.entity.JwtTokenType;

public interface JwtTokenService {

    void createJwtToken(JwtTokenDto jwtTokenDto);

    String getJwtTokenByEmail(String email, JwtTokenType type);

    boolean existsByToken(String token);

    void updateJwtToken(final JwtTokenDto jwtTokenDto);

    void deleteJwtTokenByEmail(String email);

}
