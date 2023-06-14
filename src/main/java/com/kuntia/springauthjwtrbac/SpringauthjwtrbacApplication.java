package com.kuntia.springauthjwtrbac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SpringauthjwtrbacApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringauthjwtrbacApplication.class, args);
	}

}
