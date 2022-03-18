package com.dataart.dancestudio.db.repository.impl;

import com.dataart.dancestudio.db.repository.Repository;
import com.dataart.dancestudio.db.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class LessonRepository implements Repository<LessonEntity> {

    private final JdbcTemplate jdbcTemplate;

    RowMapper<LessonEntity> rowMapper = (result, rowNumber) -> {
        LessonEntity lesson = new LessonEntity();
        lesson.setId(result.getInt("id"));
        lesson.setUserTrainer(result.getObject("user_trainer_id", UserEntity.class));
        lesson.setDanceStyle(result.getObject("dance_style_id", DanceStyleEntity.class));
        lesson.setStartDatetime(result.getObject("start_datetime", Instant.class));
        lesson.setDuration(result.getInt("duration"));
        lesson.setRoom(result.getObject("room_id", RoomEntity.class));
        lesson.setIsDeleted(result.getBoolean("is_deleted"));
        return lesson;
    };

    @Autowired
    public LessonRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void save(LessonEntity lessonEntity) {
        String sql = "INSERT INTO dancestudio.lessons(user_trainer_id, dance_style_id, start_datetime, duration, room_id, is_deleted) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, lessonEntity.getUserTrainer().getId(), lessonEntity.getDanceStyle().getId(),
                lessonEntity.getStartDatetime(), lessonEntity.getDuration(), lessonEntity.getRoom().getId(),
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

    @Override
    public void update(LessonEntity lessonEntity, int id) {
        String sql = "UPDATE dancestudio.lessons SET user_trainer_id = ?, dance_style_id = ?, start_datetime = ?, duration = ?, room_id = ?, is_deleted = ? WHERE id = ?";
        jdbcTemplate.update(sql, lessonEntity.getUserTrainer().getId(), lessonEntity.getDanceStyle().getId(),
                lessonEntity.getStartDatetime(), lessonEntity.getDuration(), lessonEntity.getRoom().getId(),
                lessonEntity.getIsDeleted(), id);
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM dancestudio.lessons WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<LessonEntity> findAll() {
        String sql = "SELECT * FROM dancestudio.lessons";
        return jdbcTemplate.query(sql, rowMapper);
    }

}
