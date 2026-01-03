package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.stats.AgeStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<User, UUID> {

    List<User> findByFirstName(String firstName);
    List<User> findByFirstNameIgnoreCase(String firstName);

    @Query("""
        SELECT new com.example.demo.stats.AgeStats(
                CAST(AVG(u.age) AS double),
                CAST(MIN(u.age) AS integer),
                CAST(MAX(u.age) AS integer),
                COUNT(u)
            )
                FROM User u
    """)
    Optional<AgeStats> getGlobalAgeStats();
}