package com.dataart.dancestudio.repository.impl;

import com.dataart.dancestudio.model.entity.DanceStyleEntity;
import com.dataart.dancestudio.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DanceStyleRepository implements Repository<DanceStyleEntity> {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<DanceStyleEntity> rowMapper = (result, rowNumber) -> DanceStyleEntity.builder()
            .id(result.getInt("id"))
            .name(result.getString("name"))
            .description(result.getString("description"))
            .build();

    @Autowired
    public DanceStyleRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(final DanceStyleEntity danceStyleEntity) {
        return -1;
    }

    @Override
    public Optional<DanceStyleEntity> findById(final int id) {
        return Optional.empty();
    }

    @Override
    public void update(final DanceStyleEntity danceStyleEntity, final int id) {

    }

    @Override
    public void deleteById(final int id) {

    }

    @Override
    public List<DanceStyleEntity> findAll() {
        final String sql = "SELECT id, name, description FROM dancestudio.dance_styles";
        return jdbcTemplate.query(sql, rowMapper);
    }

}
