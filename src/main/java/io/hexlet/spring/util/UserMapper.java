package io.hexlet.spring.util;

import io.hexlet.spring.dto.UserDTO;
import io.hexlet.spring.model.User;
import org.springframework.stereotype.Component;

@Deprecated
@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
