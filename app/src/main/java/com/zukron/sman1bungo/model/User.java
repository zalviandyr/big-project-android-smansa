package com.zukron.sman1bungo.model;

import org.threeten.bp.LocalDate;

public class User {
    private String username;
    private String password;
    private String level;
    private LocalDate lastLogin;
    private LocalDate created;

    public User(String username, String password, String level, LocalDate lastLogin, LocalDate created) {
        this.username = username;
        this.password = password;
        this.level = level;
        this.lastLogin = lastLogin;
        this.created = created;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getLevel() {
        return level;
    }

    public LocalDate getLastLogin() {
        return lastLogin;
    }

    public LocalDate getCreated() {
        return created;
    }
}
