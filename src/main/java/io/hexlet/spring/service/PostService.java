package io.hexlet.spring.service;

import io.hexlet.spring.repository.PostRepository;
import io.hexlet.spring.repository.TagRepository;
import io.hexlet.spring.repository.UserRepository;
import io.hexlet.spring.dto.PostCreateDTO;
import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.dto.PostParamsDTO;
import io.hexlet.spring.dto.PostPatchDTO;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.mapper.PostMapper;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.specification.PostSpecification;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final PostMapper postMapper;
    private final PostSpecification postSpecification;

    public PostService(PostRepository postRepository, UserRepository userRepository, TagRepository tagRepository, PostMapper postMapper, PostSpecification postSpecification) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.postMapper = postMapper;
        this.postSpecification = postSpecification;
    }

    public List<PostDTO> getAll(PostParamsDTO params, int page, int size) {
        var spec = postSpecification.build(params);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("id")));
        return postRepository.findAll(spec, pageable).map(postMapper::toDTO).toList();
    }

    public PostDTO findById(Long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пост не найден"));
        return postMapper.toDTO(post);
    }

    public PostDTO create(PostCreateDTO postCreateDTO) {
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
        return postMapper.toDTO(postRepository.save(post));
    }

    public PostDTO edit(Long id,PostPatchDTO data) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пост не найден"));

        data.getTitle().ifPresent(post::setTitle);
        data.getContent().ifPresent(post::setContent);
        return postMapper.toDTO(postRepository.save(post));
    }


    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    public void addTag(Long postId, Long tagId) {
        var post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Пост не найден"));

        var tag = tagRepository.findById(tagId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Тег не найден"));

        post.addTag(tag);
    }

    @Transactional
    public void deleteTag(Long postId, Long tagId) {
        var post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Пост не найден"));

        var tag = tagRepository.findById(tagId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Тег не найден"));

        post.removeTag(tag);
    }
}
