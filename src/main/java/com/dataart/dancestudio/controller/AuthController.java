package com.dataart.dancestudio.controller;

import com.dataart.dancestudio.model.dto.UserDetailsTest;
import com.dataart.dancestudio.model.dto.UserRegistrationDto;
import com.dataart.dancestudio.model.entity.Role;
import com.dataart.dancestudio.service.UserService;
import com.dataart.dancestudio.utils.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping()
public class AuthController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(final UserService userService, final JwtTokenUtil jwtTokenUtil,
                          final AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public Map<String, Object> registerHandler(@RequestBody final UserRegistrationDto user) throws IOException {
        final int id = userService.createUser(user);
        final String token = jwtTokenUtil.generateToken(
                id, user.getEmail(), Role.of(user.getRoleId()).orElse(Role.USER).name());
        return Collections.singletonMap("jwt-token", token);
    }

    @PostMapping("/token")
    public Map<String, Object> loginHandler(@RequestBody final UserDetailsTest userDetailsDto) {
        try {
            final int id = 42;
            final UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(userDetailsDto.getEmail(), userDetailsDto.getPassword());
            authenticationManager.authenticate(authInputToken);
            final String token = jwtTokenUtil.generateToken(id, userDetailsDto.getEmail(), Role.USER.name());
            return Collections.singletonMap("jwt-token", token);
        } catch (final AuthenticationException authenticationException) {
            throw new RuntimeException("Invalid Login Credentials");
        }
    }

}
