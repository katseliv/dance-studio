package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.EntityCreationException;
import com.dataart.dancestudio.model.dto.JwtTokenDto;
import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.JwtTokenType;
import com.dataart.dancestudio.model.response.JwtResponse;
import com.dataart.dancestudio.model.response.LoginResponse;
import com.dataart.dancestudio.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenService jwtTokenService;
    private final UserDetailsService userDetailsService;

    @Override
    @Transactional
    public LoginResponse login(final UserDetailsDto userDetailsDto) {
        final String email = userDetailsDto.getEmail();
        if (jwtTokenService.existsByUserEmail(email)) {
            final String accessToken = jwtTokenService.getJwtTokenByEmailAndType(email, JwtTokenType.ACCESS);
            final String refreshToken = jwtTokenService.getJwtTokenByEmailAndType(email, JwtTokenType.REFRESH);
            log.info("User with email = {} has been logged in with pre-existing tokens.", email);
            return new LoginResponse(accessToken, refreshToken);
        }

        final String accessToken = jwtTokenProvider.generateAccessToken(userDetailsDto);
        final String refreshToken = jwtTokenProvider.generateRefreshToken(userDetailsDto);

        final JwtTokenDto jwtAccessTokenDto = JwtTokenDto.builder()
                .token(accessToken)
                .type(JwtTokenType.ACCESS)
                .email(email)
                .build();
        final JwtTokenDto jwtRefreshTokenDto = JwtTokenDto.builder()
                .token(refreshToken)
                .type(JwtTokenType.REFRESH)
                .email(email)
                .build();

        jwtTokenService.createJwtToken(jwtAccessTokenDto);
        jwtTokenService.createJwtToken(jwtRefreshTokenDto);
        log.info("User with email = {} has been logged in with new tokens.", email);
        return new LoginResponse(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public JwtResponse getNewAccessToken(final String refreshToken) {
        final String email = jwtTokenProvider.getEmail(refreshToken);
        if (email.isBlank()) {
            log.warn("Email is empty or blank.");
            throw new EntityCreationException("Refresh token is invalid. Can't create new access token.");
        }

        if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
            log.info("Token with type = {} for email = {} is valid.", email, JwtTokenType.REFRESH);
        } else {
            jwtTokenService.deleteJwtTokensByEmail(email);
            log.warn("Token with type = {} email = {} is invalid.", email, JwtTokenType.REFRESH);
            throw new EntityCreationException("Refresh token is invalid. Can't create new access token.");
        }

        final String refreshTokenFromDB = jwtTokenService.getJwtTokenByEmailAndType(email, JwtTokenType.REFRESH);
        if (!refreshTokenFromDB.equals(refreshToken)) {
            log.warn("Token with type = {} email = {} not equal to existed token in DB.", email, JwtTokenType.REFRESH);
            throw new EntityCreationException("Refresh token is invalid. Can't create new access token.");
        }

        final UserDetailsDto userDetailsDto = (UserDetailsDto) userDetailsService.loadUserByUsername(email);
        final String accessToken = jwtTokenProvider.generateAccessToken(userDetailsDto);
        final String updatedRefreshToken = jwtTokenProvider.updateIssuedAtOfRefreshToken(userDetailsDto, refreshToken);

        final JwtTokenDto jwtAccessTokenDto = JwtTokenDto.builder()
                .token(accessToken)
                .type(JwtTokenType.ACCESS)
                .email(email)
                .build();
        final JwtTokenDto jwtRefreshTokenDto = JwtTokenDto.builder()
                .token(updatedRefreshToken)
                .type(JwtTokenType.REFRESH)
                .email(email)
                .build();

        jwtTokenService.updateJwtToken(jwtAccessTokenDto);
        jwtTokenService.updateJwtToken(jwtRefreshTokenDto);
        log.info("New Access Token has been created.");
        return new JwtResponse(accessToken, updatedRefreshToken);
    }

    @Override
    @Transactional
    public void logout(final String email) {
        jwtTokenService.deleteJwtTokensByEmail(email);
        log.info("User with email = {} has been logged out.", email);
    }

}
