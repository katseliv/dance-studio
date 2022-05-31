package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.EmptyHttpEntityBodyException;
import com.dataart.dancestudio.model.request.GoogleTokenRequest;
import com.dataart.dancestudio.model.response.GoogleTokenResponse;
import com.dataart.dancestudio.model.response.GoogleUserInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Slf4j
@Service
public class GoogleHttpServiceImpl implements GoogleHttpService {

    @Value("${spring.security.oauth2.client.registration.google.token-endpoint}")
    private String tokenEndpoint;

    @Value("${spring.security.oauth2.client.registration.google.user-info-endpoint}")
    private String userInfoEndpoint;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.google.response-type}")
    private String responseType;

    @Value("${spring.security.oauth2.client.registration.google.grant-type}")
    private String grantType;

    @Value("${spring.security.oauth2.client.registration.google.location}")
    private String location;

    @Value("${spring.security.oauth2.client.registration.google.scope}")
    private String scope;


    @Override
    public String buildAuthorizationEndpointUrl() {
        return UriComponentsBuilder.fromHttpUrl(location)
                .queryParam("response_type", responseType)
                .queryParam("client_id", clientId)
                .queryParam("scope", scope)
                .queryParam("redirect_uri", redirectUri)
                .encode()
                .toUriString();
    }

    @Override
    public String getAccessToken(final String code) {
        final GoogleTokenRequest googleTokenRequest = GoogleTokenRequest.builder()
                .code(code)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectUri(redirectUri)
                .grantType(grantType)
                .build();
        log.info("Get Access Token Request = {}", googleTokenRequest);
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<GoogleTokenRequest> googleTokenRequestHttpEntity = new HttpEntity<>(googleTokenRequest);
        final Optional<GoogleTokenResponse> googleTokenResponseOptional = Optional.ofNullable(restTemplate.postForObject(
                tokenEndpoint, googleTokenRequestHttpEntity, GoogleTokenResponse.class));
        final GoogleTokenResponse googleTokenResponse = googleTokenResponseOptional
                .orElseThrow(() -> {
                    log.warn("Google Token Response to the POST request for the access token was empty.");
                    throw new EmptyHttpEntityBodyException("Google Token Response is empty!");
                });
        log.info("Get Access Token Response = {}", googleTokenResponse);
        return googleTokenResponse.getAccessToken();
    }

    @Override
    public GoogleUserInfoResponse getUserInfo(final String accessToken) {
        log.info("Get User Info Request = {}", accessToken);
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        final HttpEntity<String> request = new HttpEntity<>(headers);
        final ResponseEntity<GoogleUserInfoResponse> googleUserInfoResponseEntity = restTemplate.exchange(
                userInfoEndpoint, HttpMethod.GET, request, GoogleUserInfoResponse.class);
        final Optional<GoogleUserInfoResponse> googleUserInfoResponseOptional = Optional.ofNullable(
                googleUserInfoResponseEntity.getBody());
        final GoogleUserInfoResponse googleUserInfoResponse = googleUserInfoResponseOptional
                .orElseThrow(() -> {
                    log.warn("Google User Info Response to the POST request for the user info was empty.");
                    throw new EmptyHttpEntityBodyException("Google User Info Response is empty!");
                });
        log.info("Get User Info Response = {}", googleUserInfoResponse);
        return googleUserInfoResponse;
    }

}
