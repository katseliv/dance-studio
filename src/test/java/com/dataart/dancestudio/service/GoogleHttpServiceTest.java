package com.dataart.dancestudio.service;

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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class GoogleHttpServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Autowired
    private GoogleHttpServiceImpl googleHttpService;

    private final String clientId = "129877534425-6qk9a3sms10kicovojosigkjdagkomob.apps.googleusercontent.com";
    private final String redirectUri = "http://localhost:8080/api/v1/login/auth/google";
    private final String scope = "email profile";
    private final String accessToken = "accessToken";

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
        final String clientSecret = "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";
        final String grantType = "authorization_code";
        final String code = "some code";
        final GoogleTokenRequest googleTokenRequest = GoogleTokenRequest.builder()
                .code(code)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectUri(redirectUri)
                .grantType(grantType)
                .build();
        final String idToken = "idToken";
        final String refreshToken = "refreshToken";
        final String tokenType = "Bearer";
        final GoogleTokenResponse googleTokenResponse = GoogleTokenResponse.builder()
                .idToken(idToken)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .scope(scope)
                .tokenType(tokenType)
                .build();
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
    public void getUserInfo() {
        // given
        final String givenName = "given name";
        final String familyName = "family name";
        final String email = "email";
        final GoogleUserInfoResponse googleUserInfoResponse = GoogleUserInfoResponse.builder()
                .givenName(givenName)
                .familyName(familyName)
                .email(email)
                .build();

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

}
