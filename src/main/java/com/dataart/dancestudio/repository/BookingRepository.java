package com.dataart.dancestudio.repository;

import com.dataart.dancestudio.model.entity.BookingEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {

    @Modifying
    @Query("UPDATE bookings SET deleted = TRUE WHERE id = ?1")
    void markAsDeletedById(int id);

    @Modifying
    @Query("UPDATE bookings SET deleted = TRUE WHERE lesson.id = ?1")
    void markAsDeletedByLessonId(int lessonId);

    List<BookingEntity> findAllByUserId(int userId, Pageable pageable);

    boolean existsByUserIdAndLessonId(int userId, int lessonId);

    int countAllByUserId(int userId);

}
