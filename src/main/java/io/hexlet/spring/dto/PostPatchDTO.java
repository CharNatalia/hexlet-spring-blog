package io.hexlet.spring.dto;

import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

public class PostPatchDTO {
    private JsonNullable<String> title = JsonNullable.undefined();
    private JsonNullable<String> content = JsonNullable.undefined();
    private JsonNullable<Set<Long>> tagIds = JsonNullable.undefined();

    public JsonNullable<String> getContent() {
        return content;
    }

    public void setContent(JsonNullable<String> content) {
        this.content = content;
    }

    public JsonNullable<String> getTitle() {
        return title;
    }

    public void setTitle(JsonNullable<String> title) {
        this.title = title;
    }

    public JsonNullable<Set<Long>> getTagIds() {
        return tagIds;
    }

    public void setTagIds(JsonNullable<Set<Long>> tagIds) {
        this.tagIds = tagIds;
    }
}
