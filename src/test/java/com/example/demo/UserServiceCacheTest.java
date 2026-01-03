package com.example.demo;

import com.example.demo.repository.UserJpaRepository;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceCacheTest {

    @Autowired
    private UserService userService;

    @MockitoSpyBean
    private UserJpaRepository jpaRepository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void clearCache() {

        var cache = cacheManager.getCache("userAgeStats");
        if (cache != null) cache.clear();
    }

    @Test
    void getAgeStats_shouldHitDBOnlyOnce() {

        userService.getAgeStatistics();
        userService.getAgeStatistics();
        userService.getAgeStatistics();

        verify(jpaRepository, times(1)).getGlobalAgeStats();
    }

}
