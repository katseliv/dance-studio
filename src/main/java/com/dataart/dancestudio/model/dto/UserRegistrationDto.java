package com.dataart.dancestudio.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class UserRegistrationDto {

    private final Integer id;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final MultipartFile multipartFile;
    private final String email;
    private final String phoneNumber;
    private final String password;
    private final String passwordConfirmation;
    private final Integer roleId;
    private final String timeZone;
    private final Boolean isDeleted;

    public static UserRegistrationDtoBuilder builder() {
        return new UserRegistrationDtoBuilder();
    }

    public static class UserRegistrationDtoBuilder {

        private Integer id;
        private String username;
        private String firstName;
        private String lastName;
        private MultipartFile multipartFile;
        private String email;
        private String phoneNumber;
        private String password;
        private String passwordConfirmation;
        private Integer roleId;
        private String timeZone;
        private Boolean isDeleted;

        private boolean isUsed;

        public UserRegistrationDtoBuilder() {
        }

        public UserRegistrationDtoBuilder id(final Integer id) {
            this.id = id;
            return this;
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

        public UserRegistrationDtoBuilder multipartFile(final MultipartFile multipartFile) {
            this.multipartFile = multipartFile;
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

        public UserRegistrationDtoBuilder isDeleted(final Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public UserRegistrationDto build() {
            if (!isUsed) {
                isUsed = true;
                return new UserRegistrationDto(id, username, firstName, lastName, multipartFile, email, phoneNumber, password,
                        passwordConfirmation, roleId, timeZone, isDeleted);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}
