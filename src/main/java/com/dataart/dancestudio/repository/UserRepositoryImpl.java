package com.dataart.dancestudio.repository;

import com.dataart.dancestudio.model.entity.Role;
import com.dataart.dancestudio.model.entity.UserDetailsEntity;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.model.entity.UserRegistrationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<UserEntity> rowMapper = (result, rowNumber) -> UserEntity.builder()
            .id(result.getInt("id"))
            .username(result.getString("username"))
            .firstName(result.getString("first_name"))
            .lastName(result.getString("last_name"))
            .image(result.getBytes("image"))
            .email(result.getString("email"))
            .phoneNumber(result.getString("phone_number"))
            .roleId(result.getInt("role_id"))
            .timeZone(ZoneId.of(result.getString("time_zone")).toString())
            .isDeleted(result.getBoolean("is_deleted"))
            .build();

    private final RowMapper<UserDetailsEntity> rowDetailsMapper = (result, rowNumber) -> UserDetailsEntity.builder()
            .id(result.getInt("id"))
            .email(result.getString("email"))
            .roles(List.of(Role.of(result.getInt("role_id")).orElse(Role.USER)))
            .password(result.getString("password"))
            .build();

    @Autowired
    public UserRepositoryImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(final UserRegistrationEntity userRegistrationEntity) {
        final String sql = "INSERT INTO dancestudio.users(username, first_name, last_name, image, email, phone_number, " +
                "password, role_id, time_zone, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, userRegistrationEntity.getUsername());
            ps.setString(2, userRegistrationEntity.getFirstName());
            ps.setString(3, userRegistrationEntity.getLastName());
            ps.setObject(4, userRegistrationEntity.getImage());
            ps.setString(5, userRegistrationEntity.getEmail());
            ps.setString(6, userRegistrationEntity.getPhoneNumber());
            ps.setString(7, userRegistrationEntity.getPassword());
            ps.setInt(8, Role.USER.getId());
            ps.setObject(9, userRegistrationEntity.getTimeZone());
            ps.setBoolean(10, userRegistrationEntity.getIsDeleted());
            return ps;
        }, keyHolder);

        return Optional.ofNullable(keyHolder.getKey()).map(Number::intValue).orElseThrow();
    }

    @Override
    public Optional<UserEntity> findById(final int id) {
        final String sql = "SELECT id, username, first_name, last_name, image, email, phone_number, password, role_id, " +
                "time_zone, is_deleted " +
                "FROM dancestudio.users WHERE id = ? AND is_deleted = FALSE";
        final UserEntity user = jdbcTemplate.queryForObject(sql, rowMapper, id);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<UserDetailsEntity> findByEmail(final String email) {
        final String sql = "SELECT id, email, role_id, password " +
                "FROM dancestudio.users WHERE LOWER(email) = LOWER(?)";

        final List<UserDetailsEntity> userDetailsEntities = jdbcTemplate.query(sql, rowDetailsMapper, email);

        if (userDetailsEntities.size() <= 1) {
            return userDetailsEntities.stream().findFirst();
        }

        throw new RuntimeException("More than one email!");
    }

    @Override
    public void update(final UserEntity userEntity, final int id) {
        final String sql = "UPDATE dancestudio.users SET username = ?, first_name = ?, last_name = ?, image = ?, " +
                "email = ?, phone_number = ?, role_id = ?, time_zone = ?, is_deleted = ? WHERE id = ?";
        jdbcTemplate.update(sql, userEntity.getUsername(), userEntity.getFirstName(), userEntity.getLastName(),
                userEntity.getImage(), userEntity.getEmail(), userEntity.getPhoneNumber(), Role.USER.getId(),
                userEntity.getTimeZone(), userEntity.getIsDeleted(), id);
    }

    @Override
    public void updateWithoutPicture(final UserEntity userEntity, final int id) {
        final String sql = "UPDATE dancestudio.users SET username = ?, first_name = ?, last_name = ?, " +
                "email = ?, phone_number = ?, role_id = ?, time_zone = ?, is_deleted = ? WHERE id = ?";
        jdbcTemplate.update(sql, userEntity.getUsername(), userEntity.getFirstName(), userEntity.getLastName(),
                userEntity.getEmail(), userEntity.getPhoneNumber(), Role.USER.getId(), userEntity.getTimeZone(),
                userEntity.getIsDeleted(), id);
    }

    @Override
    public void deleteById(final int id) {
        final String sql = "UPDATE dancestudio.users SET is_deleted = ? WHERE id = ?";
        jdbcTemplate.update(sql, true, id);
    }

    @Override
    public List<UserEntity> findAll() {
        final String sql = "SELECT id, username, first_name, last_name, image, email, phone_number, password, role_id, " +
                "time_zone, is_deleted " +
                "FROM dancestudio.users WHERE is_deleted = FALSE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<UserEntity> findAllTrainers() {
        final String sql = "SELECT id, username, first_name, last_name, image, email, phone_number, password, role_id, " +
                "time_zone, is_deleted " +
                "FROM dancestudio.users WHERE role_id = ? AND is_deleted = FALSE";
        final int trainerId = 2;
        return jdbcTemplate.query(sql, rowMapper, trainerId);
    }

}
