package com.dataart.dancestudio.repository;

import com.dataart.dancestudio.exception.NotImplementedYetException;
import com.dataart.dancestudio.model.entity.RoomEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RoomRepositoryImpl implements RoomRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<RoomEntity> rowMapper = (result, rowNumber) -> RoomEntity.builder()
            .id(result.getInt("id"))
            .name(result.getString("name"))
            .description(result.getString("description"))
            .studioId(result.getInt("studio_id"))
            .isDeleted(result.getBoolean("is_deleted"))
            .build();

    @Autowired
    public RoomRepositoryImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(final RoomEntity roomEntity) {
        throw new NotImplementedYetException("Such method wasn't implemented");
    }

    @Override
    public Optional<RoomEntity> findById(final int id) {
        throw new NotImplementedYetException("Such method wasn't implemented");
    }

    @Override
    public void update(final RoomEntity roomEntity, final int id) {
        throw new NotImplementedYetException("Such method wasn't implemented");
    }

    @Override
    public void deleteById(final int id) {
        throw new NotImplementedYetException("Such method wasn't implemented");
    }

    @Override
    public List<RoomEntity> findAll() {
        final String sql = "SELECT id, name, description, studio_id, is_deleted " +
                "FROM dancestudio.rooms";
        return jdbcTemplate.query(sql, rowMapper);
    }

}
