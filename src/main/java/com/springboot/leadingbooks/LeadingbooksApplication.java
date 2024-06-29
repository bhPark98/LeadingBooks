package com.springboot.leadingbooks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LeadingbooksApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeadingbooksApplication.class, args);
	}

}
