package com.davidfrivas.store;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// This class should be used as a web controller for receiving web traffic
@Controller
public class HomeController {
    // At run-time, Spring gets the value associated with the key and injects it in the private field
    @Value("${spring.application.name}")
    private String appName;

    // When we send a request to the root of our website, we want this method to be called
    @RequestMapping("/")
    public String index() {
        System.out.println("appName: " + appName);

        // Return the name of the View that should be returned to the browser
        return "index.html";
    }
}
