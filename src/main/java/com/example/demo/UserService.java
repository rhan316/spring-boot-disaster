package com.example.demo;

import com.example.demo.stats.AgeStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class UserService {

    private final GenerateData data;
    private final UserJdbcRepository repository;
    private final UserBatchProperties properties;
    private final UserJpaRepository jpaRepository;

    public UserService(
            GenerateData data,
            UserJdbcRepository repository,
            UserBatchProperties properties,
            UserJpaRepository jpaRepository
            ) {
        this.data = data;
        this.repository = repository;
        this.properties = properties;
        this.jpaRepository = jpaRepository;
    }

    public void batch(int batchSize) {

        if (batchSize <= 0 || batchSize > properties.maxBatchSize()) {
            throw new IllegalArgumentException(
                    "Amount must be between 1 and " + properties.maxBatchSize()
            );
        }

       int jdbcBatchSize = properties.jdbcBatchSize();

        for (int i = 0; i < batchSize; i += jdbcBatchSize) {
            int currentBatchSize = Math.min(jdbcBatchSize, batchSize - i);

            var users = IntStream.range(0, currentBatchSize)
                    .mapToObj(u -> data.createUser())
                    .toList();

            repository.batchInsert(users, jdbcBatchSize);
        }
    }

    public long size() {
        return repository.size();
    }

    public List<User> getUsersByFirstName(String firstName) {
        return jpaRepository.findByFirstNameIgnoreCase(firstName);
    }

    public Page<User> getFirstRows(int rows) {
        return jpaRepository.findAll(PageRequest.of(0, rows));
    }

    public AgeStats getAgeStatistics() {
        return jpaRepository.getGlobalAgeStats();
    }
}