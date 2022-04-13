package com.dataart.dancestudio.repository;

import com.dataart.dancestudio.exception.NotImplementedYetException;
import com.dataart.dancestudio.model.entity.Role;
import com.dataart.dancestudio.model.entity.UserDetailsEntity;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.model.entity.UserRegistrationEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Repository<UserEntity> {

    @Override
    default int save(final UserEntity userEntity) {
        throw new NotImplementedYetException("Such method wasn't implemented");
    }

    int save(UserRegistrationEntity userRegistrationEntity);

    Optional<UserDetailsEntity> findByEmail(String email);

    void updateWithoutPicture(UserEntity userEntity, int id);

    List<UserEntity> findAllByRole(Role role);

}
