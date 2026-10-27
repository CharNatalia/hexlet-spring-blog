package io.hexlet.spring.controller;

import io.hexlet.spring.Repository.TagRepository;
import io.hexlet.spring.dto.TagCreateDTO;
import io.hexlet.spring.dto.TagDTO;
import io.hexlet.spring.dto.TagUpdateDTO;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.mapper.TagMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public TagController(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    @GetMapping
    public List<TagDTO> index() {
        return tagRepository.findAll().stream().map(tagMapper::toDTO).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDTO create(@RequestBody TagCreateDTO tagCreateDTO) {
        var tag = tagMapper.toEntity(tagCreateDTO);
        return tagMapper.toDTO(tagRepository.save(tag));
    }

    @PutMapping("/{id}")
    public TagDTO edit(@PathVariable Long id, @RequestBody TagUpdateDTO tagUpdateDTO) {
        var tag = tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        tagMapper.update(tagUpdateDTO, tag);
        return tagMapper.toDTO(tagRepository.save(tag));
    }

    @GetMapping("/{id}")
    public  TagDTO edit(@PathVariable Long id) {
        var tag = tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        return tagMapper.toDTO(tag);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        var tag = tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        for (var post : new HashSet<>(tag.getPosts())) {
            post.removeTag(tag);
        }
        tagRepository.deleteById(id);
    }

}
