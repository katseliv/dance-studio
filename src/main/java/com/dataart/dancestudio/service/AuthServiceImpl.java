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
            log.info("User was logged in with pre-existing tokens.");
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
        log.info("User was logged in with new tokens.");
        return new LoginResponse(accessToken, refreshToken);
    }

    @Override
    public JwtResponse getNewAccessToken(final String refreshToken) throws AuthException {
        if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
            final String email = jwtTokenProvider.getEmail(refreshToken);
            log.info("Refresh Token was valid.");
            if (!email.isEmpty() && !email.isBlank()) {
                final String refreshTokenFromDB = jwtTokenService.getJwtTokenByEmail(email, JwtTokenType.REFRESH);
                log.info("Email wasn't empty or blank.");
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
                    log.info("New Access Token was created.");
                    return new JwtResponse(accessToken, updatedRefreshToken);
                }
            }
        } else {
            final String email = jwtTokenProvider.getEmail(refreshToken);
            jwtTokenService.deleteJwtTokensByEmail(email);
            log.info("Refresh Token was invalid.");
        }
        throw new AuthException("Jwt Token Invalid!!!");
    }

    @Override
    public void logout(final String email) {
        jwtTokenService.deleteJwtTokensByEmail(email);
        log.info("User was logged out.");
    }

}
