package com.dataart.dancestudio.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.util.Optional;

@Getter
@ToString
@AllArgsConstructor
public enum Role implements GrantedAuthority {

    ADMIN(1),
    TRAINER(2),
    USER(3);

    private final int id;

    public static Optional<Role> of(final Integer id) {
        if (id == null){
            return Optional.empty();
        }

        for (final var value : Role.values()){
            if (value.id == id){
                return Optional.of(value);
            }
        }

        return Optional.empty();
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + this.name().toUpperCase();
    }

}
