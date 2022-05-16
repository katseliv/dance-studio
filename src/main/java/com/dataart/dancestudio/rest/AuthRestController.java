package com.dataart.dancestudio.rest;

import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.request.JwtRequest;
import com.dataart.dancestudio.model.request.LoginRequest;
import com.dataart.dancestudio.model.response.JwtResponse;
import com.dataart.dancestudio.model.response.LoginResponse;
import com.dataart.dancestudio.service.AuthService;
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

import javax.security.auth.message.AuthException;

@RestController
@RequestMapping("/api/v1")
public class AuthRestController {

    private final AuthService authService;
    private final SecurityContextFacade securityContextFacade;
    protected AuthenticationManager authenticationManager;

    @Autowired
    public AuthRestController(final AuthService authService, final SecurityContextFacade securityContextFacade,
                              final AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.securityContextFacade = securityContextFacade;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody final LoginRequest loginRequest) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        securityContextFacade.getContext().setAuthentication(authentication);

        final UserDetailsDto userDetailsDto = (UserDetailsDto) authentication.getPrincipal();
        final LoginResponse loginResponse = authService.login(userDetailsDto);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/accessToken")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody final JwtRequest jwtRequest) throws AuthException {
        final JwtResponse jwtResponse = authService.getNewAccessToken(jwtRequest.getRefreshToken());
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        final String email = (String) securityContextFacade.getContext().getAuthentication().getPrincipal();
        authService.logout(email);
        return ResponseEntity.ok("Logged out successfully!!!");
    }

}
