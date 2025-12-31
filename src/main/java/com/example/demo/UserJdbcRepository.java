package com.example.demo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsert(List<User> users, int batchSize) {

        String sql = """
                INSERT INTO users (email, password, first_name, last_name, created_at)
                VALUES (?, ?, ?, ?, ?)
                """;

        jdbcTemplate.batchUpdate(
                sql, users, batchSize, (ps, user) -> {
                    ps.setString(1, user.getEmail());
                    ps.setString(2, user.getPassword());
                    ps.setString(3, user.getFirstName());
                    ps.setString(4, user.getLastName());
                    ps.setObject(5, user.getCreatedAt());
                }
        );
    }

    public long size() {
        String sql = """
                SELECT pg_relation_size('users');
                """;
        Long result = jdbcTemplate.queryForObject(sql, Long.class);

        return result != null ? result : 0L;
    }
}