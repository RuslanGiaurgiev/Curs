package com.dealer.model;

import java.util.Set;

public class User {
    private Long id;
    private String username;
    private String passwordHash;
    private Set<Role> roles;

    public User() {}

    public User(Long id, String username, String passwordHash, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.roles = roles;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
}
