package com.example.myrestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;

@SpringBootApplication
public class MyRestApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(MyRestApiApplication.class, args);
	}
}
