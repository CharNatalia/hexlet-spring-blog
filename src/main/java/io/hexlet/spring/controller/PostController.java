package io.hexlet.spring.controller;

import io.hexlet.spring.Repository.PostRepository;
import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.util.PostMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public PostController(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @GetMapping("/posts")
    public Page<PostDTO> getPublishedPosts(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));
        return postRepository.findByPublishedTrue(pageable).map(postMapper::toDTO);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDTO> show(@PathVariable Long id) {
        return postRepository.findById(id).map(post -> ResponseEntity.ok(postMapper.toDTO(post))).orElseThrow(() -> new ResourceNotFoundException("Пост не найден"));
    }

    @PostMapping("/posts")
    public ResponseEntity<PostDTO> create(@Valid @RequestBody Post post) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postMapper.toDTO(postRepository.save(post)));
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<PostDTO> edit(@PathVariable Long id, @Valid @RequestBody Post data) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пост не найден"));
        post.setTitle(data.getTitle());
        post.setPublished(data.isPublished());
        post.setContent(data.getContent());


        return ResponseEntity.status(HttpStatus.OK).body(postMapper.toDTO(postRepository.save(post)));
    }

    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        postRepository.deleteById(id);
    }
}
