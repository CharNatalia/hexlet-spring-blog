package io.hexlet.spring.controller;

import io.hexlet.spring.Repository.PostRepository;
import io.hexlet.spring.Repository.TagRepository;
import io.hexlet.spring.Repository.UserRepository;
import io.hexlet.spring.dto.PostCreateDTO;
import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.dto.PostPatchDTO;
import io.hexlet.spring.dto.PostUpdateDTO;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.mapper.JsonNullableMapper;
import io.hexlet.spring.mapper.PostMapper;
import io.hexlet.spring.model.Post;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final PostMapper postMapper;

    public PostController(PostRepository postRepository, UserRepository userRepository, TagRepository tagRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
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
    public ResponseEntity<PostDTO> create(@Valid @RequestBody PostCreateDTO postCreateDTO) {
        Post post = postMapper.toEntity(postCreateDTO);
        var user =
                userRepository
                        .findById(postCreateDTO.getUserId())
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "User not found"));
        post.setAuthor(user);


        if (postCreateDTO.getTagIds() != null) {
            for (Long tagId : postCreateDTO.getTagIds()) {
                var tag = tagRepository.findById(tagId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Тег не найден: " + tagId));

                post.addTag(tag);
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postMapper.toDTO(postRepository.save(post)));
    }

    @PatchMapping("/posts/{id}")
    public ResponseEntity<PostDTO> edit(@PathVariable Long id, @Valid @RequestBody PostPatchDTO data) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пост не найден"));

        data.getTitle().ifPresent(post::setTitle);
        data.getContent().ifPresent(post::setContent);
        return ResponseEntity.status(HttpStatus.OK).body(postMapper.toDTO(postRepository.save(post)));
    }

    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        postRepository.deleteById(id);
    }

    @PostMapping("/posts/{postId}/tags/{tagId}")
    @Transactional
    public void addTag(@PathVariable Long postId, @PathVariable Long tagId) {
        var post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Пост не найден"));

        var tag = tagRepository.findById(tagId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Тег не найден"));

        post.addTag(tag);
    }

    @DeleteMapping("/posts/{postId}/tags/{tagId}")
    @Transactional
    public void deleteTag(@PathVariable Long postId, @PathVariable Long tagId) {
        var post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Пост не найден"));

        var tag = tagRepository.findById(tagId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Тег не найден"));

        post.removeTag(tag);
    }


}
