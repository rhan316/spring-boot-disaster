package com.example.demo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsert(List<User> users, int batchSize) {

        String sql = """
                INSERT INTO users (id, email, password, first_name, last_name, created_at, age)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.batchUpdate(
                sql, users, batchSize, (ps, user) -> {
                    ps.setObject(1, UUID.randomUUID());
                    ps.setString(2, user.getEmail());
                    ps.setString(3, user.getPassword());
                    ps.setString(4, user.getFirstName());
                    ps.setString(5, user.getLastName());
                    ps.setObject(6, user.getCreatedAt());
                    ps.setInt(7, user.getAge());
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