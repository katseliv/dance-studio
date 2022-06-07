package com.dataart.dancestudio.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(builder = LoginRequest.LoginRequestBuilder.class)
public class LoginRequest {

    @NotNull(message = "Email is null.")
    @NotBlank(message = "Email is blank.")
    @Email(message = "Email invalid.")
    private String email;

    @NotNull(message = "Password is null.")
    @NotBlank(message = "Password is blank.")
    private String password;

    @JsonPOJOBuilder(withPrefix = "")
    public static class LoginRequestBuilder {

    }

}
