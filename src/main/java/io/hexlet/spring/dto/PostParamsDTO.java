package io.hexlet.spring.dto;

import java.time.LocalDate;
import java.util.List;

public class PostParamsDTO {
    private String nameCont;
    private Long authorId;
    private LocalDate createdAtGt;
    private LocalDate createdAtLt;
    private Long tagId;
    private List<Long> tagsIn;

    public String getNameCont() {
        return nameCont;
    }

    public void setNameCont(String nameCont) {
        this.nameCont = nameCont;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public LocalDate getCreatedAtGt() {
        return createdAtGt;
    }

    public void setCreatedAtGt(LocalDate createdAtGt) {
        this.createdAtGt = createdAtGt;
    }

    public LocalDate getCreatedAtLt() {
        return createdAtLt;
    }

    public void setCreatedAtLt(LocalDate createdAtLt) {
        this.createdAtLt = createdAtLt;
    }

    public List<Long> getTagsIn() {
        return tagsIn;
    }

    public void setTagsIn(List<Long> tagsIn) {
        this.tagsIn = tagsIn;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
}
