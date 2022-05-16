package com.dataart.dancestudio.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@JsonDeserialize(builder = JwtRequest.JwtRequestBuilder.class)
public class JwtRequest {

    private final String refreshToken;

    @JsonPOJOBuilder(withPrefix = "")
    public static class JwtRequestBuilder {

    }

}
