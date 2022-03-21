package com.dataart.dancestudio.db.repository.impl;

import com.dataart.dancestudio.db.repository.Repository;
import com.dataart.dancestudio.db.entity.RoleEntity;
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

    RowMapper<UserEntity> rowMapper = (result, rowNumber) -> {
        UserEntity user = new UserEntity();
        user.setId(result.getInt("id"));
        user.setUsername(result.getString("username"));
        user.setFirstName(result.getString("first_name"));
        user.setLastName(result.getString("last_name"));
        user.setImage(result.getBytes("image"));
        user.setEmail(result.getString("email"));
        user.setPhoneNumber(result.getString("phone_number"));
        user.setPassword(result.getString("password"));
        user.setRole(result.getObject("role_id", RoleEntity.class));
        user.setTimeZone(result.getObject("time_zone", ZoneId.class).toString());
        user.setIsDeleted(result.getBoolean("is_deleted"));
        return user;
    };

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
                userEntity.getRole().getId(), userEntity.getTimeZone(), userEntity.getIsDeleted(), id);
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM dancestudio.users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<UserEntity> findAll() {
        String sql = "SELECT * FROM dancestudio.users";
        return jdbcTemplate.query(sql, rowMapper);
    }

}
