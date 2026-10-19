package io.hexlet.spring.controller;

import io.hexlet.spring.model.Post;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SpringBootApplication
@RestController
public class PostController {
    private List<Post> posts = new ArrayList<>();
    private Integer postId;

    @GetMapping("/posts")
    public List<Post> index(@RequestParam(defaultValue = "10") Integer limit) {
        return posts.stream().limit(limit).toList();
    }

    @GetMapping("/posts/{id}")
    public Optional<Post> show(@PathVariable Long id) {
        return posts.stream()
                .filter(p -> Objects.equals(p.getId(), id))
                .findFirst();
    }

    @PostMapping("/posts")
    public Post create(@RequestBody Post post) {
        if (post.getTitle() == null || post.getTitle().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Заголовок не должен быть пустым");
        }

        if (post.getContent() == null || post.getContent().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Содержимое не должно быть пустым");
        }
        post.setCreatedAt(LocalDateTime.now());
        posts.add(post);
        return post;
    }

    @PutMapping("/posts/{id}")
    public Post edit(@PathVariable Long id, @RequestBody Post data) {
        var post = posts.stream()
                .filter(p -> Objects.equals(p.getId(), id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (data.getTitle() == null || data.getTitle().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Заголовок не должен быть пустым");
        }

        if (data.getContent() == null || data.getContent().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Содержимое не должно быть пустым");
        }

        post.setTitle(data.getTitle());
        post.setAuthor(data.getAuthor());
        post.setContent(data.getContent());
        post.setCreatedAt(LocalDateTime.now());

        return post;
    }

    @DeleteMapping("/posts/{id}")
    public void delete(@PathVariable Long id) {
        posts.removeIf(p -> Objects.equals(p.getId(), id));
    }







}
