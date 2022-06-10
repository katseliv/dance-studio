package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.JwtTokenDto;
import com.dataart.dancestudio.model.JwtTokenType;

public interface JwtTokenService {

    void createJwtToken(JwtTokenDto jwtTokenDto);

    String getJwtTokenByEmailAndType(String email, JwtTokenType type);

    boolean existsByToken(String token);

    boolean existsByUserEmail(String email);

    void updateJwtToken(final JwtTokenDto jwtTokenDto);

    void deleteJwtTokensByEmail(String email);

}
