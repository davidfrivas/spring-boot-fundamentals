package com.davidfrivas.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class StoreApplication {

	public static void main(String[] args) {
		// the `run` method returns an object of type ApplicationContext (IoC container that stores objects)
		ApplicationContext context = SpringApplication.run(StoreApplication.class, args);

		// Get a bean of type UserService managed by Spring
		var userService = context.getBean(UserService.class);
		userService.registerUser(new User(1L, "johndoe@gmail.com", "password123", "John Doe"));
		//userService.registerUser(new User(1L, "johndoe@gmail.com", "password123", "John Doe"));

	}

}
