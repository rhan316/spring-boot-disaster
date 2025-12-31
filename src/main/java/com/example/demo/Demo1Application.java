package com.example.demo;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.List;

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

		System.out.println();
		System.out.println();
		System.out.println("=====================================");
		System.out.println("Size of table: " + userService.size());
		System.out.println();

		userService.getFirstRows(100)
				.forEach(System.out::println);

	}
}
