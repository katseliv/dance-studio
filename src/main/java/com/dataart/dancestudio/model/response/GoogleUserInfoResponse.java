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
@JsonDeserialize(builder = GoogleUserInfoResponse.GoogleUserInfoResponseBuilder.class)
public class GoogleUserInfoResponse {

    private final String sub;
    private final String name;
    @JsonProperty("given_name")
    private final String givenName;
    @JsonProperty("family_name")
    private final String familyName;
    private final String picture;
    private final String email;
    @JsonProperty("email_verified")
    private final Boolean emailVerified;
    private final String local;

    @JsonPOJOBuilder(withPrefix = "")
    public static class GoogleUserInfoResponseBuilder {

    }

}