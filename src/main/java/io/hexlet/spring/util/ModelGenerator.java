package io.hexlet.spring.util;

import io.hexlet.spring.repository.PostRepository;
import io.hexlet.spring.repository.TagRepository;
import io.hexlet.spring.repository.UserRepository;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.model.Tag;
import io.hexlet.spring.model.User;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Profile("dev")
public class ModelGenerator {
    private final Faker faker;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PasswordEncoder passwordEncoder;

    public ModelGenerator(Faker faker, UserRepository userRepository, PostRepository postRepository, TagRepository tagRepository, PasswordEncoder passwordEncoder) {
        this.faker = faker;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void generateData() {
        List<Tag> tags = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            var tag = new Tag();
            tag.setName("Тег " + (i + 1));
            tags.add(tagRepository.save(tag));
        }

        for (var i = 0; i <= 10; i++) {
            var user = new User();
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setBirthday(faker.timeAndDate().birthday());
            user.setEmail(faker.internet().emailAddress());
            user.setPasswordDigest(passwordEncoder.encode("password123"));
            userRepository.save(user);
            userRepository.save(user);



            var post = new Post();
            post.setAuthor(user);
            post.setTitle(faker.name().title());
            String content = faker.lorem().paragraph();
            post.setContent(content.substring(0, Math.min(content.length(), 255)));
            post.setPublished(faker.bool().bool());

            var shuffledTags = new ArrayList<>(tags);
            Collections.shuffle(shuffledTags);

            int count = faker.number().numberBetween(1, tags.size() + 1);

            for (int j = 0; j < count; j++) {
                post.addTag(shuffledTags.get(j));
            }

            postRepository.save(post);
        }
    }
}
