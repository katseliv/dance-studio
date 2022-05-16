package com.dataart.dancestudio.repository;

import com.dataart.dancestudio.model.entity.JwtTokenEntity;
import com.dataart.dancestudio.model.entity.JwtTokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtTokenEntity, Integer> {

    @Modifying
    @Query("UPDATE jwt_tokens SET isDeleted = TRUE " +
            "WHERE user.id = (SELECT id FROM users WHERE email = ?1) AND type = ?2")
    void markAsDeletedByUserEmailAndType(String email, JwtTokenType type);

    Optional<JwtTokenEntity> findByUserEmailAndType(String email, JwtTokenType type);

    boolean existsByToken(String token);

    boolean existsByUserEmailAndType(String email, JwtTokenType type);

}
