package io.hexlet.spring.specification;

import io.hexlet.spring.dto.PostParamsDTO;
import io.hexlet.spring.model.Post;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class PostSpecification {
    public Specification<Post> build(PostParamsDTO params) {
        return withAuthorId(params.getAuthorId())
                .and(withCreatedAtGt(params.getCreatedAtGt()))
                .and(withCreatedAtLt(params.getCreatedAtLt()))
                .and(withNameCont(params.getNameCont()))
                .and(withTagId(params.getTagId()))
                .and(withTagsIn(params.getTagsIn()));
    }

    public Specification<Post> withAuthorId(Long authorId) {
        return (root, query, criteriaBuilder) ->
                authorId == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get("author").get("id"), authorId);
    }

    public Specification<Post> withCreatedAtGt(LocalDate date) {
        return (root, query, criteriaBuilder) ->
                date == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.greaterThan(root.get("createdAt"), date);
    }

    public Specification<Post> withCreatedAtLt(LocalDate date) {
        return (root, query, criteriaBuilder) ->
                date == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.lessThan(root.get("createdAt"), date);
    }

    public Specification<Post> withNameCont(String nameCont) {
        return (root, query, criteriaBuilder) ->
                nameCont == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.like(root.get("title"), "%" + nameCont + "%");
    }

    public Specification<Post> withTagId(Long tagId) {
        return (root, query, criteriaBuilder) ->
                tagId == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.join("tags").get("id"), tagId);
    }

    public Specification<Post> withTagsIn(List<Long> tags) {
        return (root, query, criteriaBuilder) -> {
            if (tags == null || tags.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            query.distinct(true);

            return root.join("tags").get("id").in(tags);
        };
    }
}
