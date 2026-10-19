package io.hexlet.spring.model;

import java.time.LocalDateTime;

public class Post {
    private static Long idNumber = 0L;
    private Long id;

    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;

    public Post(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;

        this.id = getIdNumber();
    }

    public static Long getIdNumber() {
        idNumber++;
        return idNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
