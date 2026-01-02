package com.example.demo.stats;

public record AgeStats(
        Double averageAge,
        Integer minAge,
        Integer maxAge,
        Long totalUsers
) { }
