package com.dataart.dancestudio.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public enum Role {

    ADMIN(1),
    USER(2);

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

}
