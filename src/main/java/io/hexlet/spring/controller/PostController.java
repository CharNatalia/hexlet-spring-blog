package io.hexlet.spring.controller;

import io.hexlet.spring.model.Post;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
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
    public ResponseEntity<List<Post>>  index(@RequestParam(defaultValue = "10") Integer limit) {
        var result = posts.stream().limit(limit).toList();
        return ResponseEntity.ok()
                .header("X-Total_Caunt", String.valueOf(result.size()))
                .body(result);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> show(@PathVariable Long id) {
        var post = posts.stream()
                .filter(p -> Objects.equals(p.getId(), id))
                .findFirst();
        return ResponseEntity.of(post);
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> create(@RequestBody Post post) {
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
        var location = URI.create("/posts/" + post.getId());
        return ResponseEntity.created(location).body(post);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> edit(@PathVariable Long id, @RequestBody Post data) {
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

        return ResponseEntity.accepted().body(post);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        posts.removeIf(p -> Objects.equals(p.getId(), id));
        return ResponseEntity.noContent().build();
    }
}
