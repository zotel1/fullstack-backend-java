package com.challenge.fullstack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@PropertySource("classpath:application.yml")
public class FullstackTechForbApplication {

	public static void main(String[] args) {
		SpringApplication.run(FullstackTechForbApplication.class, args);
	}

}
