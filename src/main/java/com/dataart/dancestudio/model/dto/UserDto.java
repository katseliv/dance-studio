package com.dataart.dancestudio.model.dto;

import com.dataart.dancestudio.annotation.ImageValid;
import com.dataart.dancestudio.annotation.TimeZoneValid;
import com.dataart.dancestudio.annotation.UsernameValid;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.*;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@JsonDeserialize(builder = UserDto.UserDtoBuilder.class)
public class UserDto {

    @NotNull(message = "Username is null.")
    @NotBlank(message = "Username is blank.")
    @UsernameValid
    @Pattern(regexp = ".*([A-Z]|[a-z]).*", message = "Username must contain a letter.")
    private final String username;

    @NotNull(message = "First Name is null.")
    @NotBlank(message = "First Name is blank.")
    @Pattern(regexp = "^([A-Z]|[a-z])+$", message = "First Name mustn't contain a number.")
    private final String firstName;

    @NotNull(message = "Last Name is null.")
    @NotBlank(message = "Last Name is blank.")
    @Pattern(regexp = "^([A-Z]|[a-z])+$", message = "Last Name mustn't contain a number.")
    private final String lastName;

    @NotNull(message = "Image is null.")
    @NotBlank(message = "Image is blank.")
    @ImageValid
    private final String base64StringImage;

    @NotNull(message = "Email is null.")
    @NotBlank(message = "Email is blank.")
    @Email(message = "Email invalid.")
    private final String email;

    @NotNull(message = "Phone Number is null.")
    @NotBlank(message = "Phone Number is blank.")
    @Pattern(regexp = "^([0-9])+$", message = "Phone Number mustn't contain a letter.")
    @Size(min = 7, max = 11, message = "Phone Number is out of range {7, 11}.")
    private final String phoneNumber;

    @NotNull(message = "Role Id is null.")
    @Positive(message = "Role Id is negative ot zero.")
    private final Integer roleId;

    @TimeZoneValid
    private final String timeZone;

    @JsonPOJOBuilder(withPrefix = "")
    public static class UserDtoBuilder {

    }

}
