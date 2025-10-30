package com.davidfrivas.mouse_colony_app;

import com.davidfrivas.mouse_colony_app.entities.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MouseColonyAppApplication {

	public static void main(String[] args) {
		// the `run` method returns an object of type ApplicationContext (IoC container that stores objects)
		//ApplicationContext context = SpringApplication.run(StoreApplication.class, args);
        User u = User.builder()
                .name("David")
                .password("1234")
                .email("asd@123.com")
                .build(); // build method

        System.out.println(u.getEmail());
	}

}
