package com.dataart.dancestudio.rest;

import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.dto.UserRegistrationDto;
import com.dataart.dancestudio.model.request.LoginRequest;
import com.dataart.dancestudio.model.response.LoginResponse;
import com.dataart.dancestudio.service.UserService;
import com.dataart.dancestudio.utils.JwtTokenUtil;
import com.dataart.dancestudio.utils.SecurityContextFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class AuthRestController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final SecurityContextFacade securityContextFacade;
    protected AuthenticationManager authenticationManager;

    @Autowired
    public AuthRestController(final UserService userService, final JwtTokenUtil jwtTokenUtil,
                              final SecurityContextFacade securityContextFacade,
                              final AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.securityContextFacade = securityContextFacade;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public int register(@RequestBody @Valid final UserRegistrationDto userRegistrationDto) throws IOException {
        return userService.createUser(userRegistrationDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody final LoginRequest loginRequest) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        securityContextFacade.getContext().setAuthentication(authentication);

        final UserDetailsDto userDetailsDto = (UserDetailsDto) authentication.getPrincipal();
        final String jwtToken = jwtTokenUtil.generateAccessToken(userDetailsDto);

        return ResponseEntity.ok(LoginResponse.builder().jwtToken(jwtToken).build());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        securityContextFacade.getContext().setAuthentication(null);
        return ResponseEntity.ok("Log out successful");
    }

}
