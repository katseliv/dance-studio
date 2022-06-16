package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.EntityCreationException;
import com.dataart.dancestudio.model.dto.JwtTokenDto;
import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.JwtTokenType;
import com.dataart.dancestudio.model.Role;
import com.dataart.dancestudio.model.response.JwtResponse;
import com.dataart.dancestudio.model.response.LoginResponse;
import com.dataart.dancestudio.provider.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private JwtTokenProvider jwtTokenProviderMock;

    @Mock
    private JwtTokenService jwtTokenServiceMock;

    @Mock
    private UserDetailsService userDetailsServiceMock;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    private final String email = "email@email.com";
    private final String password = "1234";

    final UserDetailsDto userDetailsDto = UserDetailsDto.builder()
            .email(email)
            .password(password)
            .roles(List.of(Role.USER))
            .build();

    private final String accessToken = "access";
    private final String refreshToken = "refresh";
    private final String newAccessToken = "newAccess";
    private final String newRefreshToken = "newRefresh";

    final LoginResponse loginResponse = LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();

    final JwtResponse jwtResponse = JwtResponse.builder()
            .accessToken(newAccessToken)
            .refreshToken(newRefreshToken)
            .build();

    private final JwtTokenDto jwtAccessTokenDto = JwtTokenDto.builder()
            .token(newAccessToken)
            .type(JwtTokenType.ACCESS)
            .email(email)
            .build();

    private final JwtTokenDto jwtRefreshTokenDto = JwtTokenDto.builder()
            .token(newRefreshToken)
            .type(JwtTokenType.REFRESH)
            .email(email)
            .build();

    @Test
    public void loginWhenAccessAndRefreshTokensExist() {
        // given
        when(jwtTokenServiceMock.existsByUserEmail(email)).thenReturn(true);
        when(jwtTokenServiceMock.getJwtTokenByEmailAndType(email, JwtTokenType.ACCESS)).thenReturn(accessToken);
        when(jwtTokenServiceMock.getJwtTokenByEmailAndType(email, JwtTokenType.REFRESH)).thenReturn(refreshToken);

        // when
        final LoginResponse loginResponseActual = authServiceImpl.login(userDetailsDto);

        // then
        verify(jwtTokenServiceMock, times(1)).existsByUserEmail(email);
        verify(jwtTokenServiceMock, times(1)).getJwtTokenByEmailAndType(email, JwtTokenType.ACCESS);
        verify(jwtTokenServiceMock, times(1)).getJwtTokenByEmailAndType(email, JwtTokenType.REFRESH);
        assertEquals(loginResponse, loginResponseActual);
    }

    @Test
    public void loginWhenAccessAndRefreshTokensDoNotExist() {
        // given
        when(jwtTokenServiceMock.existsByUserEmail(email)).thenReturn(false);
        when(jwtTokenProviderMock.generateAccessToken(userDetailsDto)).thenReturn(accessToken);
        when(jwtTokenProviderMock.generateRefreshToken(userDetailsDto)).thenReturn(refreshToken);

        // when
        final LoginResponse loginResponseActual = authServiceImpl.login(userDetailsDto);

        // then
        verify(jwtTokenServiceMock, times(1)).existsByUserEmail(email);
        verify(jwtTokenProviderMock, times(1)).generateAccessToken(userDetailsDto);
        verify(jwtTokenProviderMock, times(1)).generateRefreshToken(userDetailsDto);
        assertEquals(loginResponse, loginResponseActual);
    }

    @Test
    public void getNewAccessToken() {
        // given
        when(jwtTokenProviderMock.getEmail(refreshToken)).thenReturn(email);
        when(jwtTokenProviderMock.validateRefreshToken(refreshToken)).thenReturn(true);
        when(jwtTokenServiceMock.getJwtTokenByEmailAndType(email, JwtTokenType.REFRESH)).thenReturn(refreshToken);
        when(userDetailsServiceMock.loadUserByUsername(email)).thenReturn(userDetailsDto);
        when(jwtTokenProviderMock.generateAccessToken(userDetailsDto)).thenReturn(newAccessToken);
        when(jwtTokenProviderMock.updateIssuedAtOfRefreshToken(userDetailsDto, refreshToken)).thenReturn(newRefreshToken);

        // when
        final JwtResponse jwtResponseActual = authServiceImpl.getNewAccessToken(refreshToken);

        // then
        verify(userDetailsServiceMock, times(1)).loadUserByUsername(email);
        verify(jwtTokenProviderMock, times(1)).generateAccessToken(userDetailsDto);
        verify(jwtTokenProviderMock, times(1)).updateIssuedAtOfRefreshToken(userDetailsDto, refreshToken);
        verify(jwtTokenServiceMock, times(1)).updateJwtToken(jwtAccessTokenDto);
        verify(jwtTokenServiceMock, times(1)).updateJwtToken(jwtRefreshTokenDto);
        assertEquals(jwtResponse, jwtResponseActual);
    }

    @Test
    public void getNewAccessTokenWhenEmailIsBlank() {
        // given
        when(jwtTokenProviderMock.getEmail(refreshToken)).thenReturn(" ");

        // when then
        final var actualException = assertThrows(EntityCreationException.class,
                () -> authServiceImpl.getNewAccessToken(refreshToken));
        verify(userDetailsServiceMock, never()).loadUserByUsername(email);
        verify(jwtTokenProviderMock, never()).generateAccessToken(userDetailsDto);
        verify(jwtTokenProviderMock, never()).updateIssuedAtOfRefreshToken(userDetailsDto, refreshToken);
        verify(jwtTokenServiceMock, never()).updateJwtToken(jwtAccessTokenDto);
        verify(jwtTokenServiceMock, never()).updateJwtToken(jwtRefreshTokenDto);
        assertEquals(actualException.getMessage(), "Refresh token is invalid. Can't create new access token.");
    }

    @Test
    public void getNewAccessTokenWhenTokenInvalid() {
        // given
        when(jwtTokenProviderMock.getEmail(refreshToken)).thenReturn(email);
        when(jwtTokenProviderMock.validateRefreshToken(refreshToken)).thenReturn(false);

        // when then
        final var actualException = assertThrows(EntityCreationException.class,
                () -> authServiceImpl.getNewAccessToken(refreshToken));
        verify(userDetailsServiceMock, never()).loadUserByUsername(email);
        verify(jwtTokenProviderMock, never()).generateAccessToken(userDetailsDto);
        verify(jwtTokenProviderMock, never()).updateIssuedAtOfRefreshToken(userDetailsDto, refreshToken);
        verify(jwtTokenServiceMock, never()).updateJwtToken(jwtAccessTokenDto);
        verify(jwtTokenServiceMock, never()).updateJwtToken(jwtRefreshTokenDto);
        assertEquals(actualException.getMessage(), "Refresh token is invalid. Can't create new access token.");
    }

    @Test
    public void getNewAccessTokenWhenRefreshTokenNotEqualWithRefreshTokenFromDB() {
        // given
        when(jwtTokenProviderMock.getEmail(refreshToken)).thenReturn(email);
        when(jwtTokenProviderMock.validateRefreshToken(refreshToken)).thenReturn(true);
        when(jwtTokenServiceMock.getJwtTokenByEmailAndType(email, JwtTokenType.REFRESH)).thenReturn(newRefreshToken);

        // when then
        final var actualException = assertThrows(EntityCreationException.class,
                () -> authServiceImpl.getNewAccessToken(refreshToken));
        verify(userDetailsServiceMock, never()).loadUserByUsername(email);
        verify(jwtTokenProviderMock, never()).generateAccessToken(userDetailsDto);
        verify(jwtTokenProviderMock, never()).updateIssuedAtOfRefreshToken(userDetailsDto, refreshToken);
        verify(jwtTokenServiceMock, never()).updateJwtToken(jwtAccessTokenDto);
        verify(jwtTokenServiceMock, never()).updateJwtToken(jwtRefreshTokenDto);
        assertEquals(actualException.getMessage(), "Refresh token is invalid. Can't create new access token.");
    }

    @Test
    public void logout() {
        // given
        doNothing().when(jwtTokenServiceMock).deleteJwtTokensByEmail(email);

        // when
        authServiceImpl.logout(email);

        // then
        verify(jwtTokenServiceMock, times(1)).deleteJwtTokensByEmail(email);
    }

}
