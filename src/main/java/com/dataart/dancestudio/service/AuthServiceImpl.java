package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.JwtTokenDto;
import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.entity.JwtTokenType;
import com.dataart.dancestudio.model.response.JwtResponse;
import com.dataart.dancestudio.model.response.LoginResponse;
import com.dataart.dancestudio.provider.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenService jwtTokenService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthServiceImpl(final JwtTokenProvider jwtTokenProvider, final JwtTokenService jwtTokenService,
                           final UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtTokenService = jwtTokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public LoginResponse login(final UserDetailsDto userDetailsDto) {
        final String email = userDetailsDto.getEmail();
        if (jwtTokenService.existsByUserEmail(email)) {
            final String accessToken = jwtTokenService.getJwtTokenByEmail(email, JwtTokenType.ACCESS);
            final String refreshToken = jwtTokenService.getJwtTokenByEmail(email, JwtTokenType.REFRESH);
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
    public JwtResponse getNewAccessToken(final String refreshToken) throws AuthException {
        final String email = jwtTokenProvider.getEmail(refreshToken);
        if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
            log.info("Token with type = {} for email = {} has been valid.", email, JwtTokenType.REFRESH);
        } else {
            jwtTokenService.deleteJwtTokensByEmail(email);
            log.warn("Token with type = {} email = {} has been invalid.", email, JwtTokenType.REFRESH);
            throw new AuthException("Jwt Token invalid!");
        }

        if (email.isEmpty() || email.isBlank()) {
            log.warn("Email is empty or blank.");
            throw new AuthException("Jwt Token invalid!");
        }

        final String refreshTokenFromDB = jwtTokenService.getJwtTokenByEmail(email, JwtTokenType.REFRESH);
        if (refreshTokenFromDB.equals(refreshToken)) {
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
        } else {
            log.warn("Token with type = {} email = {} has been invalid.", email, JwtTokenType.REFRESH);
            throw new AuthException("Jwt Token invalid!");
        }
    }

    @Override
    public void logout(final String email) {
        jwtTokenService.deleteJwtTokensByEmail(email);
        log.info("User with email = {} has been logged out.", email);
    }

}
