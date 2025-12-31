package com.example.demo;

import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

@Component
public class GenerateData {

    private final Faker faker;

    public GenerateData(Faker faker) {
        this.faker = faker;
    }

    public User createUser() {

        var user = new User();

        user.setPassword(faker.credentials().password());
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        user.setEmail(faker.internet().safeEmailAddress(user.getFirstName() + "." + user.getLastName())
        );
        user.setCreatedAt(faker.timeAndDate()
                .past(120, TimeUnit.DAYS)
                .atOffset(ZoneOffset.UTC)
        );

        return user;
    }
}