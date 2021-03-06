package com.dataart.dancestudio.repository;

import com.dataart.dancestudio.model.Role;
import com.dataart.dancestudio.model.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @Modifying
    @Query("UPDATE users SET deleted = TRUE WHERE id = ?1")
    void markAsDeletedById(final int id);

    Optional<UserEntity> findByEmail(String email);

    @Modifying
    @Query("SELECT DISTINCT u.email " +
            "FROM users u " +
            "LEFT JOIN bookings b ON u.id = b.user.id " +
            "LEFT JOIN lessons l ON b.lesson.id = l.id " +
            "WHERE l.id = ?1")
    List<String> findAllEmailsByLessonId(int lessonId);

    List<UserEntity> findAllByRole(Role role, Pageable pageable);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
