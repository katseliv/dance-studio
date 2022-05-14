package com.dataart.dancestudio.service;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.dataart.dancestudio.model.dto.JwtTokenDto;
import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.entity.JwtTokenType;
import com.dataart.dancestudio.model.response.JwtResponse;
import com.dataart.dancestudio.model.response.LoginResponse;
import com.dataart.dancestudio.provider.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;

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
        final String accessToken = jwtTokenProvider.generateAccessToken(userDetailsDto);
        final String refreshToken = jwtTokenProvider.generateRefreshToken(userDetailsDto);
        final String email = userDetailsDto.getEmail();

        final JwtTokenDto jwtAccessTokenDto = JwtTokenDto.builder()
                .token(accessToken)
                .type(JwtTokenType.ACCESS)
                .email(email)
                .isDeleted(false)
                .build();
        final JwtTokenDto jwtRefreshTokenDto = JwtTokenDto.builder()
                .token(refreshToken)
                .type(JwtTokenType.REFRESH)
                .email(email)
                .isDeleted(false)
                .build();

        jwtTokenService.createJwtToken(jwtAccessTokenDto);
        jwtTokenService.createJwtToken(jwtRefreshTokenDto);
        return new LoginResponse(accessToken, refreshToken);
    }

    @Override
    public JwtResponse getNewAccessToken(final String refreshToken) throws AuthException {
        try {
            if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
                final String email = jwtTokenProvider.getEmail(refreshToken);
                if (!email.isEmpty() && !email.isBlank()) {
                    final String refreshTokenFromDB = jwtTokenService.getJwtTokenByEmail(email, JwtTokenType.REFRESH);
                    if (refreshTokenFromDB.equals(refreshToken)) {
                        final UserDetailsDto userDetailsDto = (UserDetailsDto) userDetailsService.loadUserByUsername(email);
                        final String accessToken = jwtTokenProvider.generateAccessToken(userDetailsDto);

                        final JwtTokenDto jwtAccessTokenDto = JwtTokenDto.builder()
                                .token(accessToken)
                                .type(JwtTokenType.ACCESS)
                                .email(email)
                                .isDeleted(false)
                                .build();

                        jwtTokenService.updateJwtToken(jwtAccessTokenDto);
                        return new JwtResponse(accessToken, refreshToken);
                    }
                }
            }
        } catch (final TokenExpiredException exception) {
            final String email = jwtTokenProvider.getEmail(refreshToken);
            jwtTokenService.deleteJwtTokenByEmail(email);
        }
        throw new AuthException("Jwt Token Invalid!!!");
    }

    @Override
    public JwtResponse getNewRefreshToken(final String refreshToken) throws AuthException {
        if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
            final String email = jwtTokenProvider.getEmail(refreshToken);
            if (!email.isEmpty() && !email.isBlank()) {
                final String refreshTokenFromDB = jwtTokenService.getJwtTokenByEmail(email, JwtTokenType.REFRESH);
                if (refreshTokenFromDB.equals(refreshToken)) {
                    final UserDetailsDto userDetailsDto = (UserDetailsDto) userDetailsService.loadUserByUsername(email);
                    final String accessToken = jwtTokenProvider.generateAccessToken(userDetailsDto);
                    final String newRefreshToken = jwtTokenProvider.generateNewRefreshToken(userDetailsDto, refreshToken);

                    final JwtTokenDto jwtAccessTokenDto = JwtTokenDto.builder()
                            .token(accessToken)
                            .type(JwtTokenType.ACCESS)
                            .email(email)
                            .isDeleted(false)
                            .build();
                    final JwtTokenDto jwtRefreshTokenDto = JwtTokenDto.builder()
                            .token(newRefreshToken)
                            .type(JwtTokenType.REFRESH)
                            .email(email)
                            .isDeleted(false)
                            .build();

                    jwtTokenService.updateJwtToken(jwtAccessTokenDto);
                    jwtTokenService.updateJwtToken(jwtRefreshTokenDto);
                    return new JwtResponse(accessToken, newRefreshToken);
                }
            }
        }
        throw new AuthException("Jwt Token Invalid!!!");
    }

    @Override
    public void logout(final String email) {
        jwtTokenService.deleteJwtTokenByEmail(email);
    }

}
