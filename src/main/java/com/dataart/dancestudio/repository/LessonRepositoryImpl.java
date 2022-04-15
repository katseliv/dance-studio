package com.dataart.dancestudio.repository;

import com.dataart.dancestudio.model.entity.LessonEntity;
import com.dataart.dancestudio.model.entity.view.LessonViewEntity;
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
import java.util.stream.Stream;

@Component
public class LessonRepositoryImpl implements LessonRepository {

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
    public LessonRepositoryImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(final LessonEntity lessonEntity) {
        final String sql = "INSERT INTO dancestudio.lessons(user_trainer_id, dance_style_id, start_datetime, duration, " +
                "room_id, is_deleted) VALUES (?, ?, ?, ?, ?, ?)";
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
        final String sql = "SELECT id, user_trainer_id, dance_style_id, start_datetime, duration, room_id, is_deleted " +
                "FROM dancestudio.lessons WHERE id = ? AND is_deleted = FALSE";
        final LessonEntity lesson = jdbcTemplate.queryForObject(sql, rowMapper, id);
        return Optional.ofNullable(lesson);
    }

    @Override
    public Optional<LessonViewEntity> findViewById(final int id) {
        final String sql = "SELECT l.id, u.first_name, u.last_name, ds.name, l.start_datetime " +
                "FROM dancestudio.lessons l " +
                "JOIN dancestudio.users u ON u.id = l.user_trainer_id " +
                "JOIN dancestudio.dance_styles ds ON ds.id = l.dance_style_id " +
                "WHERE l.id = ? AND l.is_deleted = FALSE";
        final LessonViewEntity lesson = jdbcTemplate.queryForObject(sql, rowViewMapper, id);
        return Optional.ofNullable(lesson);
    }

    @Override
    public void update(final LessonEntity lessonEntity, final int id) {
        final String sql = "UPDATE dancestudio.lessons SET user_trainer_id = ?, dance_style_id = ?, start_datetime = ?, " +
                "duration = ?, room_id = ?, is_deleted = ? WHERE id = ?";
        jdbcTemplate.update(sql, lessonEntity.getUserTrainerId(), lessonEntity.getDanceStyleId(),
                lessonEntity.getStartDatetime(), lessonEntity.getDuration(), lessonEntity.getRoomId(),
                lessonEntity.getIsDeleted(), id);
    }

    @Override
    public void markAsDeleted(final int id) {
        final String sql = "UPDATE dancestudio.lessons SET is_deleted = ? WHERE id = ?";
        jdbcTemplate.update(sql, true, id);
    }

    @Override
    public List<LessonEntity> findAll() {
        final String sql = "SELECT id, user_trainer_id, dance_style_id, start_datetime, duration, room_id, is_deleted " +
                "FROM dancestudio.lessons WHERE is_deleted = FALSE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<LessonViewEntity> findAllViews(final String trainerName, final String danceStyleName, final String date,
                                               final int limit, final long offset) {
        final String sql = "SELECT l.id, u.first_name, u.last_name, ds.name, l.start_datetime " +
                buildSqlFilteredAllLessonsBody(trainerName, danceStyleName, date) +
                "ORDER BY l.id DESC " +
                "LIMIT ? " +
                "OFFSET ?";

        final Object[] objects = Stream.of(trainerName, danceStyleName, date, limit, offset)
                .filter(Objects::nonNull)
                .toArray();
        return jdbcTemplate.query(sql, rowViewMapper, objects);
    }

    @Override
    public List<LessonViewEntity> findAllUserLessonViews(final int userId, final int limit, final long offset) {
        final String sql = "SELECT l.id, u.first_name, u.last_name, ds.name, l.start_datetime " +
                "FROM dancestudio.lessons l " +
                "JOIN dancestudio.users u ON u.id = l.user_trainer_id " +
                "JOIN dancestudio.dance_styles ds ON ds.id = l.dance_style_id " +
                "WHERE l.user_trainer_id = ? AND l.is_deleted = FALSE " +
                "ORDER BY l.id DESC " +
                "LIMIT ? " +
                "OFFSET ?";
        return jdbcTemplate.query(sql, rowViewMapper, userId, limit, offset);
    }

    @Override
    public Optional<Integer> numberOfFilteredLessons(final String trainerName, final String danceStyleName, final String date) {
        final String sql = "SELECT COUNT(l.id) " + buildSqlFilteredAllLessonsBody(trainerName, danceStyleName, date);

        final Object[] objects = Stream.of(trainerName, danceStyleName, date)
                .filter(Objects::nonNull)
                .toArray();
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Integer.class, objects));
    }

    @Override
    public Optional<Integer> numberOfUserLessons(final int userId) {
        final String sql = "SELECT COUNT(l.id) " +
                "FROM dancestudio.lessons l " +
                "JOIN dancestudio.users u ON u.id = l.user_trainer_id " +
                "JOIN dancestudio.dance_styles ds ON ds.id = l.dance_style_id " +
                "WHERE l.user_trainer_id = ? AND l.is_deleted = FALSE";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Integer.class, userId));
    }

    private String buildSqlFilteredAllLessonsBody(final String trainerName, final String danceStyle, final String date) {
        final String sql =
                "FROM dancestudio.lessons l " +
                        "JOIN dancestudio.users u ON u.id = l.user_trainer_id " +
                        "JOIN dancestudio.dance_styles ds ON ds.id = l.dance_style_id " +
                        "WHERE l.is_deleted = FALSE ";

        final StringBuilder stringBuilder = new StringBuilder(sql);

        if (trainerName != null) {
            stringBuilder.append("AND u.first_name || ' ' || u.last_name ILIKE '%' || ? || '%' ");
        }

        if (danceStyle != null) {
            stringBuilder.append("AND ds.name ILIKE '%' || ? || '%' ");
        }

        if (date != null) {
            stringBuilder.append("AND CAST(l.start_datetime AS VARCHAR) LIKE '%' || ? || '%' ");
        }

        return stringBuilder.toString();
    }


}
