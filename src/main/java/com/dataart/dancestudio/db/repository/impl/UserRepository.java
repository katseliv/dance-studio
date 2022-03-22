package com.dataart.dancestudio.db.repository.impl;

import com.dataart.dancestudio.db.repository.Repository;
import com.dataart.dancestudio.db.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Component
public class UserRepository implements Repository<UserEntity> {

    private final JdbcTemplate jdbcTemplate;

    RowMapper<UserEntity> rowMapper = (result, rowNumber) -> UserEntity.builder()
            .id(result.getInt("id"))
            .username(result.getString("username"))
            .firstName(result.getString("first_name"))
            .lastName(result.getString("last_name"))
            .image(result.getBytes("image"))
            .email(result.getString("email"))
            .phoneNumber(result.getString("phone_number"))
            .password(result.getString("password"))
            .roleId(result.getInt("role_id"))
            .timeZone(ZoneId.of(result.getString("time_zone")).toString())
            .isDeleted(result.getBoolean("is_deleted"))
            .build();

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void save(UserEntity userEntity) {
        String sql = "INSERT INTO dancestudio.users(username, first_name, last_name, image, email, phone_number, password, role_id, time_zone, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql, userEntity.getUsername(), userEntity.getFirstName(), userEntity.getLastName(),
                userEntity.getImage(), userEntity.getEmail(), userEntity.getPhoneNumber(), userEntity.getPassword(),
                2, userEntity.getTimeZone(), userEntity.getIsDeleted());
    }

    @Override
    public Optional<UserEntity> findById(int id) {
        String sql = "SELECT * FROM dancestudio.users WHERE id = ?";
        UserEntity user = null;
        try {
            user = jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return Optional.ofNullable(user);
    }

    @Override
    public void update(UserEntity userEntity, int id) {
        String sql = "UPDATE dancestudio.users SET username = ?, first_name = ?, last_name = ?, image = ?, email = ?, phone_number = ?, password = ?, role_id = ?, time_zone = ?, is_deleted = ? WHERE id = ?";
        jdbcTemplate.update(sql, userEntity.getUsername(), userEntity.getFirstName(), userEntity.getLastName(),
                userEntity.getImage(), userEntity.getEmail(), userEntity.getPhoneNumber(), userEntity.getPassword(),
                2, userEntity.getTimeZone(), userEntity.getIsDeleted(), id);
    }

    @Override
    public void deleteById(int id) {
        String sql = "UPDATE dancestudio.users SET is_deleted = ? WHERE id = ?";
        jdbcTemplate.update(sql, true, id);
    }

    @Override
    public List<UserEntity> findAll() {
        String sql = "SELECT * FROM dancestudio.users WHERE is_deleted != TRUE";
        return jdbcTemplate.query(sql, rowMapper);
    }

}
