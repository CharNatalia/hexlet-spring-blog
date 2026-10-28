package io.hexlet.spring.controller;

import io.hexlet.spring.repository.UserRepository;
import io.hexlet.spring.dto.UserDTO;
import io.hexlet.spring.mapper.UserMapper;
import io.hexlet.spring.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Value("${app.admin-email}")
    private String adminEmail;

    @Value("${app.page-size}")
    private int page;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@RequestBody User data) {
        if (data.getEmail() == null || data.getEmail().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email не должен быть пустым");
        }

        if (data.getPassword() == null || data.getPassword().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Пароль не должен быть пустым");
        }

        var user = new User();
        user.setEmail(data.getEmail());
        user.setPasswordDigest(passwordEncoder.encode(data.getPassword()));

        return userMapper.toDTO(userRepository.save(user));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> index() {
        return userRepository.findAll().stream().map(userMapper::toDTO).toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> show(@PathVariable Long id) {
        return userRepository.findById(id).map(post -> ResponseEntity.ok(userMapper.toDTO(post))).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO update(@PathVariable Long id, @RequestBody User data) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        if ((data.getEmail() == null || data.getEmail().isBlank()) & user.getEmail() == null) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, "Заголовок не должен быть пустым");
//        }
        user.setFirstName(data.getFirstName());
        user.setLastName(data.getLastName());
        user.setEmail(data.getEmail());
        user.setBirthday(data.getBirthday());
        return userMapper.toDTO(userRepository.save(user));
    }

}
