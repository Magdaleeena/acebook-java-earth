package com.makersacademy.acebook.model;

import jakarta.persistence.*;
import lombok.Data;

import static java.lang.Boolean.TRUE;

@Data
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String display_name;
    private boolean enabled;

    public User() {
        this.enabled = TRUE;
    }

    public User(String username, String displayName) {
        this.username = username;
        this.display_name = displayName;
        this.enabled = TRUE;
    }

    public User(String username, String displayName, boolean enabled) {
        this.username = username;
        this.display_name = displayName;
        this.enabled = enabled;
    }
}
