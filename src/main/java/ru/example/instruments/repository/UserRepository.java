package ru.example.instruments.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.example.instruments.model.AppUser;

import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<AppUser> mapper = (rs, rowNum) ->
            new AppUser(
                    rs.getLong("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"));

    public Optional<AppUser> findByUsername(String username) {
        return jdbcTemplate.query(
                        "SELECT id, username, password, role FROM app_users WHERE username = ?",
                        mapper, username)
                .stream()
                .findFirst();
    }

    public boolean existsByUsername(String username) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM app_users WHERE username = ?", Integer.class, username);
        return count != null && count > 0;
    }

    public void save(String username, String encodedPassword, String role) {
        jdbcTemplate.update(
                "INSERT INTO app_users (username, password, role) VALUES (?, ?, ?)",
                username, encodedPassword, role);
    }
}
