package com.homestay.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.homestay.management.repository")

public class HomestayManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomestayManagementApplication.class, args);
	}

}
