package com.dataart.dancestudio.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserEntity {

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

    public static UserEntityBuilder builder() {
        return new UserEntityBuilder();
    }

    public static class UserEntityBuilder {

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

        public UserEntityBuilder() {
        }

        public UserEntityBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public UserEntityBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserEntityBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserEntityBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserEntityBuilder image(byte[] image) {
            this.image = image;
            return this;
        }

        public UserEntityBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserEntityBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public UserEntityBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserEntityBuilder roleId(Integer roleId) {
            this.roleId = roleId;
            return this;
        }

        public UserEntityBuilder timeZone(String timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public UserEntityBuilder isDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public UserEntity build() {
            if (!isUsed) {
                isUsed = true;
                return new UserEntity(id, username, firstName, lastName, image, email, phoneNumber, password, roleId, timeZone, isDeleted);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}
