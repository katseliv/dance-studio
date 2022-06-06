package com.dataart.dancestudio.rest;

import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.dto.UserRegistrationDto;
import com.dataart.dancestudio.model.entity.Provider;
import com.dataart.dancestudio.model.request.JwtRequest;
import com.dataart.dancestudio.model.request.LoginRequest;
import com.dataart.dancestudio.model.response.GoogleUserInfoResponse;
import com.dataart.dancestudio.model.response.JwtResponse;
import com.dataart.dancestudio.model.response.LoginResponse;
import com.dataart.dancestudio.service.AuthService;
import com.dataart.dancestudio.service.GoogleHttpService;
import com.dataart.dancestudio.service.UserService;
import com.dataart.dancestudio.utils.SecurityContextFacade;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import javax.validation.Valid;
import java.net.URI;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AuthRestController {

    private final AuthService authService;
    private final UserService userService;
    private final GoogleHttpService googleHttpService;
    private final SecurityContextFacade securityContextFacade;
    protected AuthenticationManager authenticationManager;

    @GetMapping("/login/google")
    public ResponseEntity<HttpHeaders> loginWithGoogle() {
        final HttpHeaders headers = new HttpHeaders();
        final String authorizationEndpointUrl = googleHttpService.buildAuthorizationEndpointUrl();
        headers.setLocation(URI.create(authorizationEndpointUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/login/auth/google")
    public ResponseEntity<LoginResponse> authGoogleResponse(@RequestParam final String code) {
        final String accessToken = googleHttpService.getAccessToken(code);
        final GoogleUserInfoResponse googleUserInfoResponse = googleHttpService.getUserInfo(accessToken);
        final String firstName = googleUserInfoResponse.getGivenName();
        final String lastName = googleUserInfoResponse.getFamilyName();
        final String email = googleUserInfoResponse.getEmail();
        if (userService.existsByUserEmail(email)) {
            final UserDetailsDto userDetailsDto = userService.getUserByEmail(email);
            final LoginResponse loginResponse = authService.login(userDetailsDto);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } else {
            final UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder()
                    .username(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .password("password")
                    .build();
            userService.createUser(userRegistrationDto, Provider.GOOGLE);
            final UserDetailsDto userDetailsDto = userService.getUserByEmail(email);
            final LoginResponse loginResponse = authService.login(userDetailsDto);
            return new ResponseEntity<>(loginResponse, HttpStatus.CREATED);
        }
    }

    @PostMapping("/accessToken")
    public ResponseEntity<LoginResponse> accessToken(@RequestBody @Valid final LoginRequest loginRequest) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        securityContextFacade.getContext().setAuthentication(authentication);

        final UserDetailsDto userDetailsDto = (UserDetailsDto) authentication.getPrincipal();
        final LoginResponse loginResponse = authService.login(userDetailsDto);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PostMapping("/newAccessToken")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody @Valid final JwtRequest jwtRequest) throws AuthException {
        final JwtResponse jwtResponse = authService.getNewAccessToken(jwtRequest.getRefreshToken());
        return new ResponseEntity<>(jwtResponse, HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        final String email = (String) securityContextFacade.getContext().getAuthentication().getPrincipal();
        authService.logout(email);
        return new ResponseEntity<>("Logged out successfully!", HttpStatus.OK);
    }

}
