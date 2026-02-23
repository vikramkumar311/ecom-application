package com.embarkx.FirstSpring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.embarkx.FirstSpring"})
@EnableJpaRepositories(basePackages = "com.embarkx.FirstSpring.repository")
@EntityScan(basePackages = "com.embarkx.FirstSpring.model")
public class FirstSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirstSpringApplication.class, args);
		System.out.println("Hello, Spring Boot!");
	}

}
