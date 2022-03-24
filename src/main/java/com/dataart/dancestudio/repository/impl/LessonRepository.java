package com.dataart.dancestudio.repository.impl;

import com.dataart.dancestudio.model.entity.LessonEntity;
import com.dataart.dancestudio.model.entity.view.LessonViewEntity;
import com.dataart.dancestudio.repository.Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class LessonRepository implements Repository<LessonEntity> {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<LessonEntity> rowMapper = (result, rowNumber) -> LessonEntity.builder()
            .id(result.getInt("id"))
            .userTrainerId(result.getInt("user_trainer_id"))
            .danceStyleId(result.getInt("dance_style_id"))
            .startDatetime(result.getObject("start_datetime", LocalDateTime.class))
            .duration(result.getInt("duration"))
            .roomId(result.getInt("room_id"))
            .isDeleted(result.getBoolean("is_deleted"))
            .build();

    private final RowMapper<LessonViewEntity> rowViewMapper = (result, rowNumber) -> LessonViewEntity.builder()
            .id(result.getInt("id"))
            .trainerFirstName(result.getString("first_name"))
            .trainerLastName(result.getString("last_name"))
            .danceStyleName(result.getString("name"))
            .startDatetime(result.getObject("start_datetime", LocalDateTime.class))
            .build();

    @Autowired
    public LessonRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(final LessonEntity lessonEntity) {
        final String sql = "INSERT INTO dancestudio.lessons(user_trainer_id, dance_style_id, start_datetime, duration, room_id, is_deleted) VALUES (?, ?, ?, ?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setInt(1, lessonEntity.getUserTrainerId());
            ps.setInt(2, lessonEntity.getDanceStyleId());
            ps.setObject(3, lessonEntity.getStartDatetime());
            ps.setInt(4, lessonEntity.getDuration());
            ps.setInt(5, lessonEntity.getRoomId());
            ps.setBoolean(6, lessonEntity.getIsDeleted());
            return ps;
        }, keyHolder);

        return Optional.ofNullable(keyHolder.getKey()).map(Number::intValue).orElseThrow();
    }

    @Override
    public Optional<LessonEntity> findById(final int id) {
        final String sql = "SELECT * FROM dancestudio.lessons WHERE id = ?";
        final LessonEntity lesson = jdbcTemplate.queryForObject(sql, rowMapper, id);
        return Optional.ofNullable(lesson);
    }

    public Optional<LessonViewEntity> findViewById(final int id) {
        final String sql = "SELECT l.id, u.first_name, u.last_name, ds.name, l.start_datetime FROM dancestudio.lessons l " +
                "JOIN dancestudio.users u ON u.id = l.user_trainer_id " +
                "JOIN dancestudio.dance_styles ds ON ds.id = l.dance_style_id " +
                "WHERE l.id = ? AND l.is_deleted != TRUE";
        final LessonViewEntity lesson = jdbcTemplate.queryForObject(sql, rowViewMapper, id);
        return Optional.ofNullable(lesson);
    }

    @Override
    public void update(final LessonEntity lessonEntity, final int id) {
        final LessonEntity lessonEntityFromDB = findById(id).orElseThrow();
        if (!entitiesIsEqual(lessonEntity, lessonEntityFromDB)) {
            final String sql = "UPDATE dancestudio.lessons SET user_trainer_id = ?, dance_style_id = ?, start_datetime = ?, " +
                    "duration = ?, room_id = ?, is_deleted = ? WHERE id = ?";
            jdbcTemplate.update(sql, lessonEntity.getUserTrainerId(), lessonEntity.getDanceStyleId(),
                    lessonEntity.getStartDatetime(), lessonEntity.getDuration(), lessonEntity.getRoomId(),
                    lessonEntity.getIsDeleted(), id);
        }
    }

    private boolean entitiesIsEqual(final LessonEntity lessonEntity, final LessonEntity lessonEntityFromDB) {
        return Objects.equals(lessonEntity.getUserTrainerId(), lessonEntityFromDB.getUserTrainerId()) &&
                Objects.equals(lessonEntity.getDanceStyleId(), lessonEntityFromDB.getDanceStyleId()) &&
                Objects.equals(lessonEntity.getStartDatetime(), lessonEntityFromDB.getStartDatetime()) &&
                Objects.equals(lessonEntity.getDuration(), lessonEntityFromDB.getDuration()) &&
                Objects.equals(lessonEntity.getRoomId(), lessonEntityFromDB.getRoomId());
    }

    @Override
    public void deleteById(final int id) {
        final String sql = "UPDATE dancestudio.lessons SET is_deleted = ? WHERE id = ?";
        jdbcTemplate.update(sql, true, id);
    }

    @Override
    public List<LessonEntity> findAll() {
        final String sql = "SELECT * FROM dancestudio.lessons WHERE is_deleted != TRUE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<LessonViewEntity> findAllViews() {
        final String sql = "SELECT l.id, u.first_name, u.last_name, ds.name, l.start_datetime FROM dancestudio.lessons l " +
                "JOIN dancestudio.users u ON u.id = l.user_trainer_id " +
                "JOIN dancestudio.dance_styles ds ON ds.id = l.dance_style_id " +
                "WHERE l.is_deleted != TRUE";
        return jdbcTemplate.query(sql, rowViewMapper);
    }

}
