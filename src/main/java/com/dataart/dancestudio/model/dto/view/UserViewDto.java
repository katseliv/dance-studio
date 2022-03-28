package com.dataart.dancestudio.model.dto.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserViewDto {

    private final Integer id;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String image;
    private final String email;
    private final String phoneNumber;

    public static UserViewDtoBuilder builder() {
        return new UserViewDtoBuilder();
    }

    public static class UserViewDtoBuilder {

        private Integer id;
        private String username;
        private String firstName;
        private String lastName;
        private String image;
        private String email;
        private String phoneNumber;

        private boolean isUsed;

        public UserViewDtoBuilder() {
        }

        public UserViewDtoBuilder id(final Integer id) {
            this.id = id;
            return this;
        }

        public UserViewDtoBuilder username(final String username) {
            this.username = username;
            return this;
        }

        public UserViewDtoBuilder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserViewDtoBuilder lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserViewDtoBuilder image(final String image) {
            this.image = image;
            return this;
        }

        public UserViewDtoBuilder email(final String email) {
            this.email = email;
            return this;
        }

        public UserViewDtoBuilder phoneNumber(final String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public UserViewDto build() {
            if (!isUsed) {
                isUsed = true;
                return new UserViewDto(id, username, firstName, lastName, image, email, phoneNumber);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}
