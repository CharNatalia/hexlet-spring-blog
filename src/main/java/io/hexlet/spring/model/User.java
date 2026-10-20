package io.hexlet.spring.model;

import io.hexlet.spring.util.IdGenerator;



public class User {
    private static final IdGenerator userIds = new IdGenerator();

    private Long id;
    private String name;
    private String email;

    public User(String name, String email) {
        this.email = email;
        this.id = userIds.generateId();
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
