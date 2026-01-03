package com.example.demo.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.user")
public record UserBatchProperties(
        int maxBatchSize,
        int jdbcBatchSize
) { }