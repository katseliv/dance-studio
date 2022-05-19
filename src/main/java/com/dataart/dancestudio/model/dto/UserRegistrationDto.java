package com.dataart.dancestudio.model.dto;

import com.dataart.dancestudio.annotation.PasswordMatch;
import com.dataart.dancestudio.annotation.PasswordValid;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@PasswordMatch
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@JsonDeserialize(builder = UserRegistrationDto.UserRegistrationDtoBuilder.class)
public class UserRegistrationDto {

    @NotBlank(message = "Username is blank.")
    private final String username;

    @NotBlank(message = "First Name is blank.")
    private final String firstName;

    @NotBlank(message = "Last Name is blank.")
    private final String lastName;

    @NotBlank(message = "Email is blank.")
    @Email(message = "Email invalid.")
    private final String email;

    @NotBlank(message = "Phone Number is blank.")
    @Size(min = 7, max = 11, message = "Phone Number is out of range {7, 11}.")
    private final String phoneNumber;

    @PasswordValid
    private final String password;

    @PasswordValid
    private final String passwordConfirmation;
    private final Integer roleId;
    private final String timeZone;

    public static UserRegistrationDtoBuilder builder() {
        return new UserRegistrationDtoBuilder();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class UserRegistrationDtoBuilder {

        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
        private String password;
        private String passwordConfirmation;
        private Integer roleId;
        private String timeZone;

        private boolean isUsed;

        public UserRegistrationDtoBuilder() {
        }

        public UserRegistrationDtoBuilder username(final String username) {
            this.username = username;
            return this;
        }

        public UserRegistrationDtoBuilder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserRegistrationDtoBuilder lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserRegistrationDtoBuilder email(final String email) {
            this.email = email;
            return this;
        }

        public UserRegistrationDtoBuilder phoneNumber(final String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public UserRegistrationDtoBuilder password(final String password) {
            this.password = password;
            return this;
        }

        public UserRegistrationDtoBuilder passwordConfirmation(final String passwordConfirmation) {
            this.passwordConfirmation = passwordConfirmation;
            return this;
        }

        public UserRegistrationDtoBuilder roleId(final Integer roleId) {
            this.roleId = roleId;
            return this;
        }

        public UserRegistrationDtoBuilder timeZone(final String timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public UserRegistrationDto build() {
            if (!isUsed) {
                isUsed = true;
                return new UserRegistrationDto(username, firstName, lastName, email, phoneNumber, password,
                        passwordConfirmation, roleId, timeZone);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}
