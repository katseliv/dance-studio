package com.dataart.dancestudio.model.dto;

import com.dataart.dancestudio.model.entity.Role;
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
public class UserDetailsDto implements UserDetails {

    private final Integer id;
    private final String email;
    private final List<Role> roles;
    private final String password;

    public static UserDetailsDtoBuilder builder() {
        return new UserDetailsDtoBuilder();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
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

    public static class UserDetailsDtoBuilder {

        private Integer id;
        private String email;
        private List<Role> roles;
        private String password;

        private boolean isUsed = false;

        public UserDetailsDtoBuilder() {
        }

        public UserDetailsDtoBuilder id(final Integer id) {
            this.id = id;
            return this;
        }

        public UserDetailsDtoBuilder email(final String email) {
            this.email = email;
            return this;
        }

        public UserDetailsDtoBuilder roles(final List<Role> roles) {
            this.roles = roles;
            return this;
        }

        public UserDetailsDtoBuilder password(final String password) {
            this.password = password;
            return this;
        }

        public UserDetailsDto build() {
            if (!isUsed) {
                isUsed = true;
                return new UserDetailsDto(id, email, roles, password);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}
