package com.dataart.dancestudio.model.dto;

import com.dataart.dancestudio.annotation.ImageValid;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@JsonDeserialize(builder = UserDto.UserDtoBuilder.class)
public class UserDto {

    @NotBlank(message = "Username is blank.")
    private final String username;

    @NotBlank(message = "First Name is blank.")
    private final String firstName;

    @NotBlank(message = "Last Name is blank.")
    private final String lastName;

    @ImageValid
    private final String base64StringImage;

    @NotBlank(message = "Email is blank.")
    @Email(message = "Email invalid.")
    private final String email;

    @NotBlank(message = "Phone Number is blank.")
    @Size(min = 7, max = 11, message = "Phone Number is out of range {7, 11}.")
    private final String phoneNumber;

    private final Integer roleId;
    private final String timeZone;

    public static UserDtoBuilder builder() {
        return new UserDtoBuilder();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class UserDtoBuilder {

        private String username;
        private String firstName;
        private String lastName;
        private String base64StringImage;
        private String email;
        private String phoneNumber;
        private Integer roleId;
        private String timeZone;

        private boolean isUsed;

        public UserDtoBuilder() {
        }

        public UserDtoBuilder username(final String username) {
            this.username = username;
            return this;
        }

        public UserDtoBuilder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserDtoBuilder lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserDtoBuilder base64StringImage(final String base64StringImage) {
            this.base64StringImage = base64StringImage;
            return this;
        }

        public UserDtoBuilder email(final String email) {
            this.email = email;
            return this;
        }

        public UserDtoBuilder phoneNumber(final String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public UserDtoBuilder roleId(final Integer roleId) {
            this.roleId = roleId;
            return this;
        }

        public UserDtoBuilder timeZone(final String timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public UserDto build() {
            if (!isUsed) {
                isUsed = true;
                return new UserDto(username, firstName, lastName, base64StringImage, email, phoneNumber, roleId, timeZone);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}
