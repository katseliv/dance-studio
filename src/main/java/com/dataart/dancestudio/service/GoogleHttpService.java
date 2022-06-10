package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.response.GoogleUserInfoResponse;

public interface GoogleHttpService {

    String buildAuthorizationEndpointUrl();

    String getAccessToken(String code);

    GoogleUserInfoResponse getUserInfo(String accessToken);

}
