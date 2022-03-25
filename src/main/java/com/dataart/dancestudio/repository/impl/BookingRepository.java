package com.dataart.dancestudio.repository.impl;

import com.dataart.dancestudio.repository.Repository;
import com.dataart.dancestudio.model.entity.BookingEntity;
import com.dataart.dancestudio.model.entity.LessonEntity;
import com.dataart.dancestudio.model.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookingRepository implements Repository<BookingEntity> {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<BookingEntity> rowMapper = (result, rowNumber) -> {
        BookingEntity booking = new BookingEntity();
        booking.setId(result.getInt("id"));
        booking.setUser(result.getObject("user_id", UserEntity.class));
        booking.setLesson(result.getObject("lesson_id", LessonEntity.class));
        booking.setIsDeleted(result.getBoolean("is_deleted"));
        return booking;
    };

    @Autowired
    public BookingRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(final BookingEntity bookingEntity) {
        final String sql = "INSERT INTO dancestudio.bookings(user_id, lesson_id, is_deleted) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, bookingEntity.getUser().getId(), bookingEntity.getLesson().getId(),
                bookingEntity.getIsDeleted());
    }

    @Override
    public Optional<BookingEntity> findById(final int id) {
        final String sql = "SELECT id, user_id, lesson_id, is_deleted FROM dancestudio.bookings WHERE id = ?";
        final BookingEntity booking = jdbcTemplate.queryForObject(sql, rowMapper, id);
        return Optional.ofNullable(booking);
    }

    @Override
    public void update(final BookingEntity bookingEntity, final int id) {
        final String sql = "UPDATE dancestudio.bookings SET user_id = ?, lesson_id = ?, is_deleted = ? WHERE id = ?";
        jdbcTemplate.update(sql, bookingEntity.getUser().getId(),
                bookingEntity.getLesson().getId(), bookingEntity.getIsDeleted(), id);
    }

    @Override
    public void deleteById(final int id) {
        final String sql = "DELETE FROM dancestudio.bookings WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<BookingEntity> list() {
        final String sql = "SELECT id, user_id, lesson_id, is_deleted FROM dancestudio.bookings";
        return jdbcTemplate.query(sql, rowMapper);
    }

}
