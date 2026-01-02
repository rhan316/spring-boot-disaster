package com.example.demo;

import com.example.demo.stats.AgeStats;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserJpaRepository jpaRepository;

    @Mock
    private UserJdbcRepository jdbcRepository;

    @Mock
    private GenerateData data;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnCorrectAgeStats() {

        var expectedStats = new AgeStats(30.0, 18, 50, 100L);
        when(jpaRepository.getGlobalAgeStats())
                .thenReturn(expectedStats);

        var result = userService.getAgeStatistics();

        assertThat(result.averageAge()).isEqualTo(30.0);
        assertThat(result.minAge()).isEqualTo(18);

        verify(jpaRepository, times(1)).getGlobalAgeStats();
    }
}
