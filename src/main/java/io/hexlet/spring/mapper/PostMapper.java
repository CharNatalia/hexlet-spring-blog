package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.PostCreateDTO;
import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.dto.PostUpdateDTO;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.model.Tag;
import org.mapstruct.*;

@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class PostMapper {
    @Mapping(target = "tags", ignore = true)
    public abstract Post toEntity(PostCreateDTO dto);

    @Mapping(target = "tags", ignore = true)
    public abstract void toEntity(PostUpdateDTO dto, @MappingTarget Post model);

    @Mapping(target = "userId", source = "author.id")
    public abstract PostDTO toDTO(Post model);

    public String map(Tag tag) {
        return tag.getName();
    }
}
