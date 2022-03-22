package com.dataart.dancestudio.db.repository.impl;

import com.dataart.dancestudio.db.entity.DanceStyleEntity;
import com.dataart.dancestudio.db.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DanceStyleRepository implements Repository<DanceStyleEntity> {

    private final JdbcTemplate jdbcTemplate;

    RowMapper<DanceStyleEntity> rowMapper = (result, rowNumber) -> DanceStyleEntity.builder()
            .id(result.getInt("id"))
            .name(result.getString("name"))
            .description(result.getString("description"))
            .build();

    @Autowired
    public DanceStyleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(DanceStyleEntity danceStyleEntity) {

    }

    @Override
    public Optional<DanceStyleEntity> findById(int id) {
        return Optional.empty();
    }

    @Override
    public void update(DanceStyleEntity danceStyleEntity, int id) {

    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public List<DanceStyleEntity> findAll() {
        String sql = "SELECT * FROM dancestudio.dance_styles";
        return jdbcTemplate.query(sql, rowMapper);
    }

}
