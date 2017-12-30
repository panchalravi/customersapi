package com.ca.restdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class RestdemoApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(RestdemoApplication.class, args);
	}
}
