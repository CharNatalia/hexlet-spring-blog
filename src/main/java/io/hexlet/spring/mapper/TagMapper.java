package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.*;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.model.Tag;
import org.mapstruct.*;

@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class TagMapper {
    @Mapping(target = "posts", ignore = true)
    public abstract Tag toEntity(TagCreateDTO dto);

    @Mapping(target = "posts", ignore = true)
    public abstract void update(TagUpdateDTO dto, @MappingTarget Tag model);

    public abstract TagDTO toDTO(Tag model);

    public String map(Post post) {
        return post.getTitle();
    }
}
