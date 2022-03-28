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

    public static UserViewDtoBuilder builder() {
        return new UserViewDtoBuilder();
    }

    public static class UserViewDtoBuilder {

        private Integer id;
        private String username;
        private String firstName;
        private String lastName;

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

        public UserViewDto build() {
            if (!isUsed) {
                isUsed = true;
                return new UserViewDto(id, username, firstName, lastName);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}
