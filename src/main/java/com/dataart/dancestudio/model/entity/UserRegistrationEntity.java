package com.dataart.dancestudio.model.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class UserRegistrationEntity {

    private final Integer id;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final byte[] image;
    private final String email;
    private final String phoneNumber;
    private final String password;
    private final Integer roleId;
    private final String timeZone;
    private final Boolean isDeleted;

    public static UserRegistrationEntityBuilder builder() {
        return new UserRegistrationEntityBuilder();
    }

    public static class UserRegistrationEntityBuilder {

        private Integer id;
        private String username;
        private String firstName;
        private String lastName;
        private byte[] image;
        private String email;
        private String phoneNumber;
        private String password;
        private Integer roleId;
        private String timeZone;
        private Boolean isDeleted;

        private boolean isUsed = false;

        public UserRegistrationEntityBuilder() {
        }

        public UserRegistrationEntityBuilder id(final Integer id) {
            this.id = id;
            return this;
        }

        public UserRegistrationEntityBuilder username(final String username) {
            this.username = username;
            return this;
        }

        public UserRegistrationEntityBuilder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserRegistrationEntityBuilder lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserRegistrationEntityBuilder image(final byte[] image) {
            this.image = image;
            return this;
        }

        public UserRegistrationEntityBuilder email(final String email) {
            this.email = email;
            return this;
        }

        public UserRegistrationEntityBuilder phoneNumber(final String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public UserRegistrationEntityBuilder password(final String password) {
            this.password = password;
            return this;
        }

        public UserRegistrationEntityBuilder roleId(final Integer roleId) {
            this.roleId = roleId;
            return this;
        }

        public UserRegistrationEntityBuilder timeZone(final String timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public UserRegistrationEntityBuilder isDeleted(final Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public UserRegistrationEntity build() {
            if (!isUsed) {
                isUsed = true;
                return new UserRegistrationEntity(id, username, firstName, lastName, image, email, phoneNumber, password, roleId, timeZone, isDeleted);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}
