package com.dataart.dancestudio.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@JsonDeserialize(builder = JwtRequest.JwtRequestBuilder.class)
public class JwtRequest {

    @NotNull(message = "Token is null.")
    @NotBlank(message = "Token is blank.")
    private final String refreshToken;

    @JsonPOJOBuilder(withPrefix = "")
    public static class JwtRequestBuilder {

    }

}
