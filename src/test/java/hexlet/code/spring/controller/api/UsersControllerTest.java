package hexlet.code.spring.controller.api;

import io.hexlet.spring.Application;
import io.hexlet.spring.repository.UserRepository;
import io.hexlet.spring.model.User;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class UsersControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper om;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
    }


    @Test
    public void testCreate() throws Exception {
        var body = """
                {
                    "firstName": "John",
                    "lastName": "Smith",
                    "email": "john@example.com",
                    "birthday": "1995-06-15"
                }
                """;
        var response = mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andReturn();
        String responseBody = response.getResponse().getContentAsString();
        long id = om.readTree(responseBody).get("id").asLong();
        User savedUser = userRepository.findById(id)
                .orElseThrow(() -> new AssertionError("Пользователь не сохранился в БД"));
        assertThat(savedUser.getFirstName()).isEqualTo("John");
        assertThat(savedUser.getLastName()).isEqualTo("Smith");
        assertThat(savedUser.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

    }

    @Test
    public void testUpdate() throws Exception {
        var user =
                Instancio.of(User.class)
                        .ignore(Select.field(User::getId))
                        .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                        .create();
        userRepository.save(user);

        var data = new HashMap<>();
        data.put("firstName", "Mike");

        var request =
                put("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        // ObjectMapper конвертирует Map в JSON
                        .content(om.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isOk());

        user = userRepository.findById(user.getId()).get();
        assertThat(user.getFirstName()).isEqualTo(("Mike"));
    }

    @Test
    public void testShow() throws Exception {
        var user =
                Instancio.of(User.class)
                        .ignore(Select.field(User::getId))
                        .set(Select.field(User::getEmail), "exampleTest@test.test")
                        .create();
        userRepository.save(user);

        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("exampleTest@test.test"));
    }

    @Test
    public void testDelete() throws Exception {
        var user =
                Instancio.of(User.class)
                        .ignore(Select.field(User::getId))
                        .set(Select.field(User::getEmail), "exampleTest@test.test")
                        .create();
        userRepository.save(user);

        mockMvc.perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isNoContent());
        assertTrue(userRepository.findById(user.getId()).isEmpty());


    }
}
