package com.dataart.dancestudio.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@JsonDeserialize(builder = GoogleTokenResponse.GoogleTokenResponseBuilder.class)
public class GoogleTokenResponse {

    @JsonProperty("id_token")
    private final String idToken;
    @JsonProperty("access_token")
    private final String accessToken;
    @JsonProperty("refresh_token")
    private final String refreshToken;
    @JsonProperty("expires_at")
    private final String expiresAt;
    @JsonProperty("scope")
    private final String scope;
    @JsonProperty("token_type")
    private final String tokenType;

    @JsonPOJOBuilder(withPrefix = "")
    public static class GoogleTokenResponseBuilder {

    }

}