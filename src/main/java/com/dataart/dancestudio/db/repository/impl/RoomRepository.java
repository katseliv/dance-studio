package com.dataart.dancestudio.db.repository.impl;

import com.dataart.dancestudio.db.entity.RoomEntity;
import com.dataart.dancestudio.db.entity.StudioEntity;
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

    RowMapper<RoomEntity> rowMapper = (result, rowNumber) -> {
        RoomEntity room = new RoomEntity();
        room.setId(result.getInt("id"));
        room.setName(result.getString("name"));
        room.setDescription(result.getString("description"));
        StudioEntity studio = new StudioEntity();
        studio.setId(result.getInt("studio_id"));
        room.setStudio(studio);
        room.setIsDeleted(result.getBoolean("is_deleted"));
        return room;
    };

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
