package com.davidfrivas.store;

public class User {
    private Long id;
    private String email;
    private String password;
    private String name;

    public User(Long id, String email, String password, String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
