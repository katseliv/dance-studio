package com.dataart.dancestudio.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class UserDto {

    private final Integer id;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final MultipartFile multipartFile;
    private final String email;
    private final String phoneNumber;
    private final String password;
    private final Integer roleId;
    private final String timeZone;
    private final Boolean isDeleted;

    public static UserDtoBuilder builder() {
        return new UserDtoBuilder();
    }

    public static class UserDtoBuilder {

        private Integer id;
        private String username;
        private String firstName;
        private String lastName;
        private MultipartFile multipartFile;
        private String email;
        private String phoneNumber;
        private String password;
        private Integer roleId;
        private String timeZone;
        private Boolean isDeleted;

        private boolean isUsed;

        public UserDtoBuilder() {
        }

        public UserDtoBuilder id(final Integer id) {
            this.id = id;
            return this;
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

        public UserDtoBuilder multipartFile(final MultipartFile multipartFile) {
            this.multipartFile = multipartFile;
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

        public UserDtoBuilder password(final String password) {
            this.password = password;
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

        public UserDtoBuilder isDeleted(final Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public UserDto build() {
            if (!isUsed) {
                isUsed = true;
                return new UserDto(id, username, firstName, lastName, multipartFile, email, phoneNumber, password, roleId, timeZone, isDeleted);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}
