package com.app.library;

// LAB 3: annotated for Lab 3 identification

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryApplication {
	// Entry point for the Spring Boot application.
	// SpringApplication.run boots the embedded server and starts the context.
	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}
}
