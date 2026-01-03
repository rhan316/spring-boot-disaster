package com.example.demo;

import com.example.demo.entity.UserBatchProperties;
import com.example.demo.service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@EnableConfigurationProperties(UserBatchProperties.class)
@SpringBootApplication
public class Demo1Application implements ApplicationRunner {

	private final UserService userService;

	public Demo1Application(UserService userService) {
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(Demo1Application.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		userService.batch(5_000);
		System.out.println();
		System.out.println("======================================");

		System.out.println("Min age --> " + userService.getAgeStatistics().minAge());
		System.out.println("Max age --> " + userService.getAgeStatistics().maxAge());
		System.out.println("Avg age --> " + userService.getAgeStatistics().averageAge());

		System.out.println("=======================================");

	}
}
