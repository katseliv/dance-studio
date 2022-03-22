package com.dataart.dancestudio.db.repository.impl;

import com.dataart.dancestudio.db.entity.RoomEntity;
import com.dataart.dancestudio.db.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RoomRepository implements Repository<RoomEntity> {

    private final JdbcTemplate jdbcTemplate;

    RowMapper<RoomEntity> rowMapper = (result, rowNumber) -> RoomEntity.builder()
            .id(result.getInt("id"))
            .name(result.getString("name"))
            .description(result.getString("description"))
            .studioId(result.getInt("studio_id"))
            .isDeleted(result.getBoolean("is_deleted"))
            .build();

    @Autowired
    public RoomRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(RoomEntity roomEntity) {

    }

    @Override
    public Optional<RoomEntity> findById(int id) {
        return Optional.empty();
    }

    @Override
    public void update(RoomEntity roomEntity, int id) {

    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public List<RoomEntity> findAll() {
        String sql = "SELECT * FROM dancestudio.rooms";
        return jdbcTemplate.query(sql, rowMapper);
    }

}
