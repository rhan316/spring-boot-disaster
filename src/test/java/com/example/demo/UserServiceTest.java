package com.example.demo;

import com.example.demo.exceptions.StatisticsNotAvailableException;
import com.example.demo.stats.AgeStats;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserJpaRepository jpaRepository;

    @Mock
    private UserJdbcRepository jdbcRepository;

    @Mock
    private GenerateData data;

    @Mock
    private UserBatchProperties properties;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnCorrectAgeStats() {

        // Given
        var expectedStats = new AgeStats(30.0, 18, 50, 100L);

        // When
        when(jpaRepository.getGlobalAgeStats())
                .thenReturn(Optional.of(expectedStats));
        var result = userService.getAgeStatistics();

        // Then
        assertThat(result.averageAge()).isEqualTo(30.0);
        assertThat(result.minAge()).isEqualTo(18);
        verify(jpaRepository, times(1)).getGlobalAgeStats();
    }

    @Test
    @DisplayName("Should throw exception")
    void shouldThrowExceptionWhenBatchSizeIsTooLarge() {

        // given
        when(properties.maxBatchSize()).thenReturn(1_000);
        int invalidBatchSize = 2_000;

        // when & then
        assertThatThrownBy(() -> userService.batch(invalidBatchSize))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Amount must be between 1 and 1000");

        verifyNoInteractions(data);
        verifyNoInteractions(jdbcRepository);
    }

    @Test
    @DisplayName("Powinien rzucić wyjątek, gdy baza jest pusta.")
    void shouldThrowExceptionWhenNoDataIsPresent() {

        when(jpaRepository.getGlobalAgeStats()).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getAgeStatistics())
                .isInstanceOf(StatisticsNotAvailableException.class)
                .hasMessage("No data user for stats.");
    }
}
