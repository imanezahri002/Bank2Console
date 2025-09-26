package org.example.models;

import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private String email;
    private String password;
    public enum Role{TELLER,MANAGER,AUDITOR,ADMIN}
    Role role;

    // Getters / Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}

