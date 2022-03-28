package com.dataart.dancestudio.repository.impl;

import com.dataart.dancestudio.model.entity.BookingEntity;
import com.dataart.dancestudio.model.entity.view.BookingViewEntity;
import com.dataart.dancestudio.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class BookingRepository implements Repository<BookingEntity> {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<BookingEntity> rowMapper = (result, rowNumber) -> BookingEntity.builder()
            .id(result.getInt("id"))
            .userId(result.getInt("user_id"))
            .lessonId(result.getInt("lesson_id"))
            .isDeleted(result.getBoolean("is_deleted"))
            .build();

    private final RowMapper<BookingViewEntity> rowViewMapper = (result, rowNumber) -> BookingViewEntity.builder()
            .id(result.getInt("id"))
            .firstName(result.getString("first_name"))
            .lastName(result.getString("last_name"))
            .danceStyle(result.getString("name"))
            .startDatetime(result.getObject("start_datetime", LocalDateTime.class))
            .build();

    @Autowired
    public BookingRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(final BookingEntity bookingEntity) {
        final String sql = "INSERT INTO dancestudio.bookings(user_id, lesson_id, is_deleted) VALUES (?, ?, ?)";

        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setInt(1, bookingEntity.getUserId());
            ps.setInt(2, bookingEntity.getLessonId());
            ps.setBoolean(3, bookingEntity.getIsDeleted());
            return ps;
        }, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::intValue).orElseThrow();
    }

    @Override
    public Optional<BookingEntity> findById(final int id) {
        final String sql = "SELECT id, user_id, lesson_id, is_deleted FROM dancestudio.bookings WHERE id = ?";
        final BookingEntity booking = jdbcTemplate.queryForObject(sql, rowMapper, id);
        return Optional.ofNullable(booking);
    }

    public Optional<BookingViewEntity> findViewById(final int id) {
        final String sql = "SELECT bookings.id, u.first_name, u.last_name, ds.name, l.start_datetime " +
                "FROM dancestudio.bookings " +
                "JOIN dancestudio.users u ON u.id = bookings.user_id " +
                "JOIN dancestudio.lessons l ON l.id = bookings.lesson_id " +
                "JOIN dancestudio.dance_styles ds ON ds.id = l.dance_style_id " +
                "WHERE bookings.id = ? AND bookings.is_deleted != TRUE";
        final BookingViewEntity booking = jdbcTemplate.queryForObject(sql, rowViewMapper, id);
        return Optional.ofNullable(booking);
    }

    @Override
    public void update(final BookingEntity bookingEntity, final int id) {
        final String sql = "UPDATE dancestudio.bookings SET user_id = ?, lesson_id = ?, is_deleted = ? WHERE id = ?";
        jdbcTemplate.update(sql, bookingEntity.getUserId(), bookingEntity.getLessonId(), bookingEntity.getIsDeleted(), id);
    }

    @Override
    public void deleteById(final int id) {
        final String sql = "UPDATE dancestudio.bookings SET is_deleted = ? WHERE id = ?";
        jdbcTemplate.update(sql, true, id);
    }

    @Override
    public List<BookingEntity> findAll() {
        final String sql = "SELECT id, user_id, lesson_id, is_deleted FROM dancestudio.bookings WHERE is_deleted != TRUE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<BookingViewEntity> findAllViews() {
        final String sql = "SELECT bookings.id, first_name, last_name, name, start_datetime FROM dancestudio.bookings " +
                "JOIN dancestudio.users u ON u.id = bookings.user_id " +
                "JOIN dancestudio.lessons l ON l.id = bookings.lesson_id " +
                "JOIN dancestudio.dance_styles ds ON ds.id = l.dance_style_id " +
                "WHERE bookings.is_deleted != TRUE";
        return jdbcTemplate.query(sql, rowViewMapper);
    }

}
