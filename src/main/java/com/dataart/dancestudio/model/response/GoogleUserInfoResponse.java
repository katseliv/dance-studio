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

    private String sub;
    private String name;
    @JsonProperty("given_name")
    private String givenName;
    @JsonProperty("family_name")
    private String familyName;
    private String picture;
    private String email;
    @JsonProperty("email_verified")
    private Boolean emailVerified;
    private String local;

    @JsonPOJOBuilder(withPrefix = "")
    public static class GoogleUserInfoResponseBuilder {

    }

}