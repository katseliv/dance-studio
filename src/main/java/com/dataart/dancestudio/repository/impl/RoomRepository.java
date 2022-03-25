package com.dataart.dancestudio.repository.impl;

import com.dataart.dancestudio.model.entity.RoomEntity;
import com.dataart.dancestudio.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RoomRepository implements Repository<RoomEntity> {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<RoomEntity> rowMapper = (result, rowNumber) -> RoomEntity.builder()
            .id(result.getInt("id"))
            .name(result.getString("name"))
            .description(result.getString("description"))
            .studioId(result.getInt("studio_id"))
            .isDeleted(result.getBoolean("is_deleted"))
            .build();

    @Autowired
    public RoomRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(final RoomEntity roomEntity) {
        return -1;
    }

    @Override
    public Optional<RoomEntity> findById(final int id) {
        return Optional.empty();
    }

    @Override
    public void update(final RoomEntity roomEntity, final int id) {

    }

    @Override
    public void deleteById(final int id) {

    }

    @Override
    public List<RoomEntity> list() {
        final String sql = "SELECT id, name, description, studio_id, is_deleted FROM dancestudio.rooms";
        return jdbcTemplate.query(sql, rowMapper);
    }

}
