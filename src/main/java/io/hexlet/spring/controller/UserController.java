package io.hexlet.spring.controller;

import io.hexlet.spring.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Value("${app.admin-email}")
    private String adminEmail;

    @Value("${app.page-size}")
    private int page;

    private List<User> users = new ArrayList<>();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Заголовок не должен быть пустым");
        }
        users.add(user);
        return user;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> index() {
        return users.stream().limit(page).toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> show(@PathVariable Long id) {
        var user = users.stream()
                .filter(u -> Objects.equals(id, u.getId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return Optional.of(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        users.removeIf(u -> Objects.equals(u.getId(), id));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User edit(@PathVariable Long id, @RequestBody User data) {
        var user = users.stream()
                .filter(u -> Objects.equals(u.getId(), id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (data.getEmail() == null || data.getEmail().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Заголовок не должен быть пустым");
        }
        user.setName(data.getName());
        user.setEmail(data.getEmail());
        return user;
    }

}
