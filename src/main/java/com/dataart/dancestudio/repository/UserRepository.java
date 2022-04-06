package com.dataart.dancestudio.repository;

import com.dataart.dancestudio.model.entity.UserDetailsEntity;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.model.entity.UserRegistrationEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Repository<UserEntity> {

    @Override
    default int save(final UserEntity userEntity) {
        return -1;
    }

    int save(final UserRegistrationEntity userRegistrationEntity);

    Optional<UserDetailsEntity> findByEmail(final String email);

    void updateWithoutPicture(final UserEntity userEntity, final int id);

    List<UserEntity> findAllTrainers();

}
