package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.EmptyHttpResponseException;
import com.dataart.dancestudio.exception.GoogleResponseException;
import com.dataart.dancestudio.model.request.GoogleTokenRequest;
import com.dataart.dancestudio.model.response.GoogleTokenResponse;
import com.dataart.dancestudio.model.response.GoogleUserInfoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class GoogleHttpServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Autowired
    private GoogleHttpServiceImpl googleHttpService;

    private final String clientId = "129877534425-6qk9a3sms10kicovojosigkjdagkomob.apps.googleusercontent.com";
    private final String clientSecret = "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";
    private final String redirectUri = "http://localhost:8080/api/v1/login/auth/google";
    private final String scope = "email profile";
    private final String grantType = "authorization_code";
    private final String code = "some code";
    private final String idToken = "idToken";
    private final String accessToken = "accessToken";
    private final String refreshToken = "refreshToken";
    private final String tokenType = "Bearer";

    private final GoogleTokenRequest googleTokenRequest = GoogleTokenRequest.builder()
            .code(code)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .redirectUri(redirectUri)
            .grantType(grantType)
            .build();
    private final GoogleTokenResponse googleTokenResponse = GoogleTokenResponse.builder()
            .idToken(idToken)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .scope(scope)
            .tokenType(tokenType)
            .build();

    private final String givenName = "given name";
    private final String familyName = "family name";
    private final String email = "email";
    private final GoogleUserInfoResponse googleUserInfoResponse = GoogleUserInfoResponse.builder()
            .givenName(givenName)
            .familyName(familyName)
            .email(email)
            .build();

    @BeforeEach
    void setUp() {
        googleHttpService.setRestTemplate(restTemplate);
    }

    @Test
    public void buildAuthorizationEndpointUrl() {
        // given
        final String location = "https://accounts.google.com/o/oauth2/v2/auth";
        final String responseType = "code";

        final String url = UriComponentsBuilder.fromHttpUrl(location)
                .queryParam("response_type", responseType)
                .queryParam("client_id", clientId)
                .queryParam("scope", scope)
                .queryParam("redirect_uri", redirectUri)
                .encode()
                .toUriString();

        // when
        final String urlActual = googleHttpService.buildAuthorizationEndpointUrl();

        // then
        assertEquals(url, urlActual);
    }

    @Test
    public void getAccessToken() {
        // given
        final HttpEntity<GoogleTokenRequest> googleTokenRequestHttpEntity = new HttpEntity<>(googleTokenRequest);
        final String tokenEndpoint = "https://oauth2.googleapis.com/token";
        when(restTemplate.exchange(tokenEndpoint, HttpMethod.POST, googleTokenRequestHttpEntity, GoogleTokenResponse.class))
                .thenReturn(ResponseEntity.of(Optional.of(googleTokenResponse)));

        // when
        final String accessTokenActual = googleHttpService.getAccessToken(code);

        // then
        assertTrue(new ReflectionEquals(accessToken).matches(accessTokenActual));
    }

    @Test
    public void getAccessTokenWhenStatusCodeIsBad() {
        // given
        final HttpEntity<GoogleTokenRequest> googleTokenRequestHttpEntity = new HttpEntity<>(googleTokenRequest);
        final String tokenEndpoint = "https://oauth2.googleapis.com/token";
        when(restTemplate.exchange(tokenEndpoint, HttpMethod.POST, googleTokenRequestHttpEntity, GoogleTokenResponse.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_GATEWAY));

        // when then
        final var actualException = assertThrowsExactly(GoogleResponseException.class,
                () -> googleHttpService.getAccessToken(code));
        assertEquals(actualException.getMessage(), "Response status is bad!");
    }

    @Test
    public void getAccessTokenWhenTokenIsEmpty() {
        // given
        final GoogleTokenResponse googleTokenResponse = GoogleTokenResponse.builder()
                .idToken(idToken)
                .refreshToken(refreshToken)
                .scope(scope)
                .tokenType(tokenType)
                .build();

        final HttpEntity<GoogleTokenRequest> googleTokenRequestHttpEntity = new HttpEntity<>(googleTokenRequest);
        final String tokenEndpoint = "https://oauth2.googleapis.com/token";
        when(restTemplate.exchange(tokenEndpoint, HttpMethod.POST, googleTokenRequestHttpEntity, GoogleTokenResponse.class))
                .thenReturn(ResponseEntity.of(Optional.of(googleTokenResponse)));

        // when then
        final var actualException = assertThrowsExactly(EmptyHttpResponseException.class,
                () -> googleHttpService.getAccessToken(code));
        assertEquals(actualException.getMessage(), "Google Token Response is empty!");
    }

    @Test
    public void getUserInfo() {
        // given
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        final HttpEntity<String> request = new HttpEntity<>(headers);
        final String userInfoEndpoint = "https://openidconnect.googleapis.com/v1/userinfo";
        when(restTemplate.exchange(userInfoEndpoint, HttpMethod.GET, request, GoogleUserInfoResponse.class))
                .thenReturn(ResponseEntity.of(Optional.of(googleUserInfoResponse)));

        // when
        final GoogleUserInfoResponse googleUserInfoResponseActual = googleHttpService.getUserInfo(accessToken);

        // then
        assertTrue(new ReflectionEquals(googleUserInfoResponse).matches(googleUserInfoResponseActual));
    }

    @Test
    public void getUserInfoWhenStatusCodeIsBad() {
        // given
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        final HttpEntity<String> request = new HttpEntity<>(headers);
        final String userInfoEndpoint = "https://openidconnect.googleapis.com/v1/userinfo";
        when(restTemplate.exchange(userInfoEndpoint, HttpMethod.GET, request, GoogleUserInfoResponse.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_GATEWAY));

        // when then
        final var actualException = assertThrowsExactly(GoogleResponseException.class,
                () -> googleHttpService.getUserInfo(accessToken));
        assertEquals(actualException.getMessage(), "Response status is bad!");
    }

    @Test
    public void getUserInfoWhenResponseBodyIsEmpty() {
        // given
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        final HttpEntity<String> request = new HttpEntity<>(headers);
        final String userInfoEndpoint = "https://openidconnect.googleapis.com/v1/userinfo";
        when(restTemplate.exchange(userInfoEndpoint, HttpMethod.GET, request, GoogleUserInfoResponse.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // when then
        final var actualException = assertThrowsExactly(EmptyHttpResponseException.class,
                () -> googleHttpService.getUserInfo(accessToken));
        assertEquals(actualException.getMessage(), "Google User Info Response is empty!");
    }

}
