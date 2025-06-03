package com.dealer.controller;

import com.dealer.model.User;
import com.dealer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired private UserRepository repo;
    @Autowired private PasswordEncoder encoder;

    @PostMapping("/register")
    public User register(@RequestBody User u) {
        u.setPasswordHash(encoder.encode(u.getPasswordHash()));
        u.setRoles(Set.of(u.getRoles().iterator().next()));
        return repo.save(u);
    }
}
