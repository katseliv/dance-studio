package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.EmptyHttpResponseException;
import com.dataart.dancestudio.exception.GoogleResponseException;
import com.dataart.dancestudio.model.request.GoogleTokenRequest;
import com.dataart.dancestudio.model.response.GoogleTokenResponse;
import com.dataart.dancestudio.model.response.GoogleUserInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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

    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String buildAuthorizationEndpointUrl() {
        log.info("Building authorization endpoint url...");
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
        final HttpEntity<GoogleTokenRequest> googleTokenRequestHttpEntity = new HttpEntity<>(googleTokenRequest);
        final ResponseEntity<GoogleTokenResponse> googleTokenResponseEntity = restTemplate.exchange(
                tokenEndpoint, HttpMethod.POST, googleTokenRequestHttpEntity, GoogleTokenResponse.class);

        checkResponseStatusCode(googleTokenResponseEntity.getStatusCode());

        return Optional.ofNullable(googleTokenResponseEntity.getBody())
                .map(GoogleTokenResponse::getAccessToken)
                .orElseThrow(() -> {
                    log.error("Google Token Response to the POST request for the access token was empty.");
                    throw new EmptyHttpResponseException("Google Token Response is empty!");
                });
    }

    @Override
    public GoogleUserInfoResponse getUserInfo(final String accessToken) {
        log.info("Get User Info Request = {}", accessToken);
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        final HttpEntity<String> request = new HttpEntity<>(headers);
        final ResponseEntity<GoogleUserInfoResponse> googleUserInfoResponseEntity = restTemplate.exchange(
                userInfoEndpoint, HttpMethod.GET, request, GoogleUserInfoResponse.class);

        checkResponseStatusCode(googleUserInfoResponseEntity.getStatusCode());

        return Optional.ofNullable(googleUserInfoResponseEntity.getBody())
                .orElseThrow(() -> {
                    log.error("Google User Info Response to the POST request for the user info was empty.");
                    throw new EmptyHttpResponseException("Google User Info Response is empty!");
                });
    }

    private void checkResponseStatusCode(final HttpStatus statusCode) {
        if (statusCode != HttpStatus.OK) {
            log.error("Response status = {} doesn't equal to 200 OK.", statusCode);
            throw new GoogleResponseException("Response status is bad!");
        } else {
            log.info("Response status is 200 OK.");
        }
    }

}
