package com.example.demo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    List<User> findByFirstName(String firstName);
    List<User> findByFirstNameIgnoreCase(String firstName);

    @Query("SELECT u FROM User u")
    List<User> getAllUsers(Pageable pageable);
}