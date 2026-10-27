package io.hexlet.spring.util;

import io.hexlet.spring.Repository.PostRepository;
import io.hexlet.spring.Repository.TagRepository;
import io.hexlet.spring.Repository.UserRepository;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.model.Tag;
import io.hexlet.spring.model.User;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

@Component
public class ModelGenerator {
    private final Faker faker;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    public ModelGenerator(Faker faker, UserRepository userRepository, PostRepository postRepository, TagRepository tagRepository) {
        this.faker = faker;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    @PostConstruct
    public void generateData() {
        for (var i = 0; i <= 10; i++) {
            var user = new User();
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setBirthday(faker.timeAndDate().birthday());
            user.setEmail(faker.internet().emailAddress());
            userRepository.save(user);

            var tag = new Tag();
            tag.setName(faker.dota2().hero());
            tagRepository.save(tag);

            var post = new Post();
            post.setAuthor(user);
            post.setTitle(faker.name().title());
            String content = faker.lorem().paragraph();
            post.setContent(content.substring(0, Math.min(content.length(), 255)));
            post.setPublished(faker.bool().bool());
            postRepository.save(post);
        }
    }
}
