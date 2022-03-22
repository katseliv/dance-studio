package com.dataart.dancestudio.db.repository.impl;

import com.dataart.dancestudio.db.entity.view.BookingViewEntity;
import com.dataart.dancestudio.db.repository.Repository;
import com.dataart.dancestudio.db.entity.BookingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class BookingRepository implements Repository<BookingEntity> {

    private final JdbcTemplate jdbcTemplate;

    RowMapper<BookingEntity> rowMapper = (result, rowNumber) -> BookingEntity.builder()
            .id(result.getInt("id"))
            .userId(result.getInt("user_id"))
            .lessonId(result.getInt("lesson_id"))
            .isDeleted(result.getBoolean("is_deleted"))
            .build();

    RowMapper<BookingViewEntity> rowViewMapper = (result, rowNumber) -> BookingViewEntity.builder()
            .id(result.getInt("id"))
            .firstName(result.getString("first_name"))
            .lastName(result.getString("last_name"))
            .danceStyle(result.getString("name"))
            .startDatetime(result.getObject("start_datetime", LocalDateTime.class))
            .build();

    @Autowired
    public BookingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(BookingEntity bookingEntity) {
        String sql = "INSERT INTO dancestudio.bookings(user_id, lesson_id, is_deleted) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, bookingEntity.getUserId(), bookingEntity.getLessonId(),
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

    public Optional<BookingViewEntity> findViewById(int id) {
        String sql = "SELECT bookings.id, first_name, last_name, name, start_datetime FROM dancestudio.bookings " +
                "JOIN dancestudio.users u ON u.id = bookings.user_id " +
                "JOIN dancestudio.lessons l ON l.id = bookings.lesson_id " +
                "JOIN dancestudio.dance_styles ds ON ds.id = l.dance_style_id " +
                "WHERE bookings.id = ? AND bookings.is_deleted != TRUE";
        BookingViewEntity booking = null;
        try {
            booking = jdbcTemplate.queryForObject(sql, rowViewMapper, id);
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return Optional.ofNullable(booking);
    }

    @Override
    public void update(BookingEntity bookingEntity, int id) {
        String sql = "UPDATE dancestudio.bookings SET user_id = ?, lesson_id = ?, is_deleted = ? WHERE id = ?";
        jdbcTemplate.update(sql, bookingEntity.getUserId(), bookingEntity.getLessonId(), bookingEntity.getIsDeleted(), id);
    }

    @Override
    public void deleteById(int id) {
        String sql = "UPDATE dancestudio.bookings SET is_deleted = ? WHERE id = ?";
        jdbcTemplate.update(sql, true, id);
    }

    @Override
    public List<BookingEntity> findAll() {
        String sql = "SELECT * FROM dancestudio.bookings WHERE is_deleted != TRUE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<BookingViewEntity> findAllViews() {
        String sql = "SELECT bookings.id, first_name, last_name, name, start_datetime FROM dancestudio.bookings " +
                "JOIN dancestudio.users u ON u.id = bookings.user_id " +
                "JOIN dancestudio.lessons l ON l.id = bookings.lesson_id " +
                "JOIN dancestudio.dance_styles ds ON ds.id = l.dance_style_id " +
                "WHERE bookings.is_deleted != TRUE";
        return jdbcTemplate.query(sql, rowViewMapper);
    }

}
