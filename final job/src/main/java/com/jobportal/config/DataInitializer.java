package com.jobportal.config;

import com.jobportal.entity.User;
import com.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        // Create Default Admin if it doesn't exist
        if (!userService.findByEmail("admin@jobportal.com").isPresent()) {
            User admin = new User();
            admin.setName("System Admin");
            admin.setEmail("admin@jobportal.com");
            admin.setPassword("admin123");
            admin.setRole("ADMIN");
            userService.saveUser(admin);
            System.out.println("Default Admin account created: admin@jobportal.com / admin123");
        }
    }
}
