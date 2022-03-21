package com.dataart.dancestudio.db.repository.impl;

import com.dataart.dancestudio.db.repository.Repository;
import com.dataart.dancestudio.db.entity.BookingEntity;
import com.dataart.dancestudio.db.entity.LessonEntity;
import com.dataart.dancestudio.db.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookingRepository implements Repository<BookingEntity> {

    private final JdbcTemplate jdbcTemplate;

    RowMapper<BookingEntity> rowMapper = (result, rowNumber) -> {
        BookingEntity booking = new BookingEntity();
        booking.setId(result.getInt("id"));
        booking.setUser(result.getObject("user_id", UserEntity.class));
        booking.setLesson(result.getObject("lesson_id", LessonEntity.class));
        booking.setIsDeleted(result.getBoolean("is_deleted"));
        return booking;
    };

    @Autowired
    public BookingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(BookingEntity bookingEntity) {
        String sql = "INSERT INTO dancestudio.bookings(user_id, lesson_id, is_deleted) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, bookingEntity.getUser().getId(), bookingEntity.getLesson().getId(),
                bookingEntity.getIsDeleted());
    }

    @Override
    public Optional<BookingEntity> findById(int id) {
        String sql = "SELECT * FROM dancestudio.bookings WHERE id = ?";
        BookingEntity booking = null;
        try {
            booking = jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return Optional.ofNullable(booking);
    }

    @Override
    public void update(BookingEntity bookingEntity, int id) {
        String sql = "UPDATE dancestudio.bookings SET user_id = ?, lesson_id = ?, is_deleted = ? WHERE id = ?";
        jdbcTemplate.update(sql, bookingEntity.getUser().getId(),
                bookingEntity.getLesson().getId(), bookingEntity.getIsDeleted(), id);
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM dancestudio.bookings WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<BookingEntity> findAll() {
        String sql = "SELECT * FROM dancestudio.bookings";
        return jdbcTemplate.query(sql, rowMapper);
    }

}
