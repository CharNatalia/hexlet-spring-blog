package io.hexlet.spring.dto;


import java.util.Set;

public class TagUpdateDTO {
    private String name;
    private Set<Long> postIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Long> getPostIds() {
        return postIds;
    }

    public void setPostIds(Set<Long> postIds) {
        this.postIds = postIds;
    }
}
