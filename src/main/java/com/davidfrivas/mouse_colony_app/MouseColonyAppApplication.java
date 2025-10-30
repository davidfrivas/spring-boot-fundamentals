package com.davidfrivas.mouse_colony_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MouseColonyAppApplication {

	public static void main(String[] args) {
		// the `run` method returns an object of type ApplicationContext (IoC container that stores objects)
		ApplicationContext context = SpringApplication.run(MouseColonyAppApplication.class, args);
	}

}
