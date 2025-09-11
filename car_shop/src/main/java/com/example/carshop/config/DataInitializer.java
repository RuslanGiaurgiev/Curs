package com.example.carshop.config;

import com.example.carshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        if (!userService.existsByEmail("admin@carshop.ru")) {
            userService.createAdmin("admin", "admin@carshop.ru", "admin123");
        }
    }
}