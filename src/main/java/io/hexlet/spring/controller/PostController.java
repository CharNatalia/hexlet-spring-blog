package io.hexlet.spring.controller;

import io.hexlet.spring.dto.PostCreateDTO;
import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.dto.PostParamsDTO;
import io.hexlet.spring.dto.PostPatchDTO;
import io.hexlet.spring.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDTO>> index(
            PostParamsDTO params,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        var posts = postService.getAll(params, page, size);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(posts.size()))
                .body(posts);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDTO> show(@PathVariable Long id) {
        var post = postService.findById(id);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/posts")
    public ResponseEntity<PostDTO> create(@Valid @RequestBody PostCreateDTO postCreateDTO) {
        var newPost = postService.create(postCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPost);
    }

    @PatchMapping("/posts/{id}")
    public ResponseEntity<PostDTO> edit(@PathVariable Long id, @Valid @RequestBody PostPatchDTO data) {
        var newPost = postService.edit(id, data);
        return ResponseEntity.ok(newPost);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/posts/{postId}/tags/{tagId}")
    public ResponseEntity<Void> addTag(@PathVariable Long postId, @PathVariable Long tagId) {
        postService.addTag(postId, tagId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/posts/{postId}/tags/{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long postId, @PathVariable Long tagId) {
        postService.deleteTag(postId, tagId);
        return ResponseEntity.ok().build();
    }


}
