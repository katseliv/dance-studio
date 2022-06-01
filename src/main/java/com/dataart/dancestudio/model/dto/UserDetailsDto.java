package com.dataart.dancestudio.model.dto;

import com.dataart.dancestudio.model.entity.Role;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@JsonDeserialize(builder = UserDetailsDto.UserDetailsDtoBuilder.class)
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

    @JsonPOJOBuilder(withPrefix = "")
    public static class UserDetailsDtoBuilder {

    }

}
