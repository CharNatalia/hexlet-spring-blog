package hexlet.code.spring.controller.api;

import io.hexlet.spring.Application;
import io.hexlet.spring.repository.PostRepository;
import io.hexlet.spring.repository.UserRepository;
import io.hexlet.spring.model.Post;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class PostsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        postRepository.deleteAll();
    }

    @Test
    public void testCreate() throws Exception {
        var user = new User();
        user.setFirstName("John");
        user.setLastName("Smith");
        user.setEmail("john@example.com");
        user = userRepository.save(user);

        var data = new HashMap<>();
        data.put("title", "test");
        data.put("content", "test create method");
        data.put("userId", user.getId());

        var request =
                post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        // ObjectMapper конвертирует Map в JSON
                        .content(om.writeValueAsString(data));

        var response = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.content").value("test create method"))
                .andReturn();

        String responseBody = response.getResponse().getContentAsString();
        long id = om.readTree(responseBody).get("id").asLong();
        Post savedPost = postRepository.findById(id)
                .orElseThrow(() -> new AssertionError("Пост не сохранился в БД"));
        assertThat(savedPost.getTitle()).isEqualTo("test");
        assertThat(savedPost.getContent()).isEqualTo("test create method");
        assertFalse(savedPost.isPublished());
    }


    @Test
    public void testIndex() throws Exception {
        for (var i = 0; i < 5; i++) {
            var post =
                    Instancio.of(Post.class)
                            .ignore(Select.field(Post::getId))
                            .supply(Select.field(Post::getTitle), () -> faker.name().title())
                            .supply(Select.field(Post::getContent), () -> faker.text().text(200))
                            .set(Select.field(Post::isPublished), true)
                            .create();
            postRepository.save(post);
        }
        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(5)) //на странице
                .andExpect(jsonPath("$.totalElements").value(5)); //всего на всех страницах

    }

    @Test
    public void testUpdate() throws Exception {
        var post =
                Instancio.of(Post.class)
                        .ignore(Select.field(Post::getId))
                        .supply(Select.field(Post::getTitle), () -> faker.name().title())
                        .supply(Select.field(Post::getContent), () -> faker.text().text(200))
                        .create();
        postRepository.save(post);

        var data = new HashMap<>();
        data.put("title", "test");
        data.put("content", "test update method");

        var request =
                put("/api/posts/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        // ObjectMapper конвертирует Map в JSON
                        .content(om.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isOk());

        var newPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new AssertionError("Пост не сохранился в БД"));
        assertThat(newPost.getTitle()).isEqualTo("test");
        assertThat(newPost.getContent()).isEqualTo("test update method");
    }

    @Test
    public void testShow() throws Exception {
        var post =
                Instancio.of(Post.class)
                        .ignore(Select.field(Post::getId))
                        .set(Select.field(Post::getTitle), "test")
                        .set(Select.field(Post::getContent), "test show method")
                        .create();
        postRepository.save(post);

        mockMvc.perform(get("/api/posts/" + post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.content").value("test show method"));
    }

    @Test
    public void testDelete() throws Exception {
        var post =
                Instancio.of(Post.class)
                        .ignore(Select.field(Post::getId))
                        .supply(Select.field(Post::getTitle), () -> faker.name().title())
                        .supply(Select.field(Post::getContent), () -> faker.text().text(200))
                        .create();
        postRepository.save(post);
        mockMvc.perform(delete("/api/posts/" + post.getId()))
                .andExpect(status().isNoContent());
    }
}


