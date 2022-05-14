package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.response.JwtResponse;
import com.dataart.dancestudio.model.response.LoginResponse;

import javax.security.auth.message.AuthException;

public interface AuthService {

    LoginResponse login(UserDetailsDto userDetailsDto);

    JwtResponse getNewAccessToken(String refreshToken) throws AuthException;

    JwtResponse getNewRefreshToken(String refreshToken) throws AuthException;

    void logout(String email);

}
