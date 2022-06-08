package com.dataart.dancestudio.oauth;

import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.dto.UserRegistrationDto;
import com.dataart.dancestudio.model.entity.Provider;
import com.dataart.dancestudio.model.response.GoogleUserInfoResponse;
import com.dataart.dancestudio.model.response.LoginResponse;
import com.dataart.dancestudio.service.AuthService;
import com.dataart.dancestudio.service.GoogleHttpService;
import com.dataart.dancestudio.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/api/v1")
public class GoogleAuthController {

    private final AuthService authService;
    private final UserService userService;
    private final GoogleHttpService googleHttpService;

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
            final String defaultPassword = "password";
            final UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder()
                    .username(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .password(defaultPassword)
                    .build();
            final int userId = userService.createUser(userRegistrationDto, Provider.GOOGLE);
            final UserDetailsDto userDetailsDto = userService.getUserDetailsById(userId);
            final LoginResponse loginResponse = authService.login(userDetailsDto);
            return new ResponseEntity<>(loginResponse, HttpStatus.CREATED);
        }
    }

}
