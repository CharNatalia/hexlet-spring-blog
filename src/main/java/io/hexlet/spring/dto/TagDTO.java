package io.hexlet.spring.dto;

import java.util.ArrayList;
import java.util.List;

public class TagDTO {
    private Long id;
    private String name;
    private List<String> posts = new ArrayList<>();

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


    public List<String> getPosts() {
        return posts;
    }

    public void setPosts(List<String> posts) {
        this.posts = posts;
    }
}
