package com.example.demo;

import com.example.demo.entity.GenerateData;
import com.example.demo.entity.User;
import com.example.demo.entity.UserBatchProperties;
import com.example.demo.exceptions.StatisticsNotAvailableException;
import com.example.demo.repository.UserJdbcRepository;
import com.example.demo.repository.UserJpaRepository;
import com.example.demo.service.UserService;
import com.example.demo.stats.AgeStats;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Test
    void shouldReturnSomething() {

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("invisigal@email.com");
        user.setPassword("pass");
        user.setFirstName("Courtney");
        user.setLastName("Invisigal");
        user.setCreatedAt(OffsetDateTime.now());

        // Given
        when(jpaRepository.findByFirstNameIgnoreCase("Courtney")).thenReturn(List.of(user));

        // When
        var result = userService.getUsersByFirstName("Courtney");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getFirstName()).isEqualTo("Courtney");

        verify(jpaRepository, times(1)).findByFirstNameIgnoreCase("Courtney");


    }
}
