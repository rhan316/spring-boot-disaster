package com.example.demo;

import com.example.demo.service.UserService;
import com.example.demo.stats.AgeStats;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Import(TestContainersConfig.class)
public class UserIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void shouldReturnRealStatsFromPostgresContainer() {

        AgeStats stats = userService.getAgeStatistics();

        assertThat(stats).isNotNull();
        assertThat(stats.totalUsers()).isEqualTo(0L);
    }
}
