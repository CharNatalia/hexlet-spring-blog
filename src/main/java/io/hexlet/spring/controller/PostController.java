package io.hexlet.spring.controller;

import io.hexlet.spring.Repository.PostRepository;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.model.Post;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    PostRepository postRepository;

    @GetMapping("/posts")
    public List<Post> index(@RequestParam(defaultValue = "10") Integer limit) {
        return postRepository.findAll().stream().limit(limit).toList();
    }

    @GetMapping("/posts/{id}")
    public Post show(@PathVariable Long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Пост не найден"));
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> create(@Valid @RequestBody Post post) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postRepository.save(post));
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> edit(@PathVariable Long id, @Valid @RequestBody Post data) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пост не найден"));


        post.setTitle(data.getTitle());
        post.setPublished(data.isPublished());
        post.setContent(data.getContent());

        return ResponseEntity.status(HttpStatus.OK).body(postRepository.save(post));
    }

    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        postRepository.deleteById(id);
    }
}
