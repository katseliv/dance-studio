package com.dataart.dancestudio.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(builder = LoginRequest.LoginRequestBuilder.class)
public class LoginRequest {

    private String email;
    private String password;

    @JsonPOJOBuilder(withPrefix = "")
    public static class LoginRequestBuilder {

    }

}
