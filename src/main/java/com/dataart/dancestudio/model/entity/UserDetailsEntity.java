package com.dataart.dancestudio.model.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class UserDetailsEntity implements UserDetails {

    private final Integer id;
    private final String email;
    private final List<Role> roles;
    private final String password;
    private final String passwordConfirmation;

    public static UserRegistrationDtoBuilder builder() {
        return new UserRegistrationDtoBuilder();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static class UserRegistrationDtoBuilder {

        private Integer id;
        private String email;
        private List<Role> roles;
        private String password;
        private String passwordConfirmation;

        private boolean isUsed = false;

        public UserRegistrationDtoBuilder() {
        }

        public UserRegistrationDtoBuilder id(final Integer id) {
            this.id = id;
            return this;
        }

        public UserRegistrationDtoBuilder email(final String email) {
            this.email = email;
            return this;
        }

        public UserRegistrationDtoBuilder roles(final List<Role> roles) {
            this.roles = roles;
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

        public UserDetailsEntity build() {
            if (!isUsed) {
                isUsed = true;
                return new UserDetailsEntity(id, email, roles, password, passwordConfirmation);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}
