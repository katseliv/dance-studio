package com.dataart.dancestudio.db.repository.impl;

import com.dataart.dancestudio.db.entity.view.LessonViewEntity;
import com.dataart.dancestudio.db.repository.Repository;
import com.dataart.dancestudio.db.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class LessonRepository implements Repository<LessonEntity> {

    private final JdbcTemplate jdbcTemplate;

    RowMapper<LessonEntity> rowMapper = (result, rowNumber) -> LessonEntity.builder()
            .id(result.getInt("id"))
            .userTrainerId(result.getInt("user_trainer_id"))
            .danceStyleId(result.getInt("dance_style_id"))
            .startDatetime(result.getObject("start_datetime", LocalDateTime.class))
            .duration(result.getInt("duration"))
            .roomId(result.getInt("room_id"))
            .isDeleted(result.getBoolean("is_deleted"))
            .build();

    RowMapper<LessonViewEntity> rowViewMapper = (result, rowNumber) -> LessonViewEntity.builder()
            .id(result.getInt("id"))
            .trainerFirstName(result.getString("first_name"))
            .trainerLastName(result.getString("last_name"))
            .danceStyleName(result.getString("name"))
            .startDatetime(result.getObject("start_datetime", LocalDateTime.class))
            .build();

    @Autowired
    public LessonRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void save(LessonEntity lessonEntity) {
        String sql = "INSERT INTO dancestudio.lessons(user_trainer_id, dance_style_id, start_datetime, duration, room_id, is_deleted) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, lessonEntity.getUserTrainerId(), lessonEntity.getDanceStyleId(),
                lessonEntity.getStartDatetime(), lessonEntity.getDuration(), lessonEntity.getRoomId(),
                lessonEntity.getIsDeleted());
    }

    @Override
    public Optional<LessonEntity> findById(int id) {
        String sql = "SELECT * FROM dancestudio.lessons WHERE id = ?";
        LessonEntity lesson = null;
        try {
            lesson = jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return Optional.ofNullable(lesson);
    }

    public Optional<LessonViewEntity> findViewById(int id) {
        String sql = "SELECT lessons.id, first_name, last_name, name, start_datetime FROM dancestudio.lessons " +
                "join dancestudio.users u on u.id = lessons.user_trainer_id " +
                "join dancestudio.dance_styles ds on ds.id = lessons.dance_style_id " +
                "WHERE lessons.id = ? AND lessons.is_deleted != TRUE";
        LessonViewEntity lesson = null;
        try {
            lesson = jdbcTemplate.queryForObject(sql, rowViewMapper, id);
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return Optional.ofNullable(lesson);
    }

    @Override
    public void update(LessonEntity lessonEntity, int id) {
        String sql = "UPDATE dancestudio.lessons SET user_trainer_id = ?, dance_style_id = ?, start_datetime = ?, duration = ?, room_id = ?, is_deleted = ? WHERE id = ?";
        jdbcTemplate.update(sql, lessonEntity.getUserTrainerId(), lessonEntity.getDanceStyleId(),
                lessonEntity.getStartDatetime(), lessonEntity.getDuration(), lessonEntity.getRoomId(),
                lessonEntity.getIsDeleted(), id);
    }

    @Override
    public void deleteById(int id) {
        String sql = "UPDATE dancestudio.lessons SET is_deleted = ? WHERE id = ?";
        jdbcTemplate.update(sql, true, id);
    }

    @Override
    public List<LessonEntity> findAll() {
        String sql = "SELECT * FROM dancestudio.lessons WHERE is_deleted != TRUE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<LessonViewEntity> findAllViews() {
        String sql = "SELECT lessons.id, first_name, last_name, name, start_datetime FROM dancestudio.lessons " +
                "join dancestudio.users u on u.id = lessons.user_trainer_id " +
                "join dancestudio.dance_styles ds on ds.id = lessons.dance_style_id " +
                "WHERE lessons.is_deleted != TRUE";
        return jdbcTemplate.query(sql, rowViewMapper);
    }

}
