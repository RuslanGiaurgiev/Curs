package com.dealer;

import com.dealer.model.Role;
import com.dealer.model.User;
import com.dealer.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class DealerServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DealerServerApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedAdmin(UserRepository users, PasswordEncoder encoder) {
        return args -> {
            if (users.count() == 0) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPasswordHash(encoder.encode("admin123"));
                admin.setRoles(Set.of(Role.ADMIN, Role.USER));
                users.save(admin);
                System.out.println("Seeded admin/admin123");
            }
        };
    }
}
