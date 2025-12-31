package com.example.demo;

import net.datafaker.Faker;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigFaker {

    @Bean
    public Faker faker() {
        return new Faker();
    }
}