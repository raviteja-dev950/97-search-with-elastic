package com.search.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "document", indexes = {
    @Index(name = "idx_title", columnList = "title"),
    @Index(name = "idx_content", columnList = "content")
})
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @Column(length = 5000)
    private String content;

    private String tags;

    private LocalDateTime indexedAt;

    private Long viewCount = 0L;

    public Document() {}

    // Builder Pattern - Same as 96 Video.Builder - Tier 10 Requirement
    public static class Builder {
        private String title;
        private String content;
        private String tags;
        private LocalDateTime indexedAt;
        private Long viewCount = 0L;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder tags(String tags) {
            this.tags = tags;
            return this;
        }

        public Builder indexedAt(LocalDateTime indexedAt) {
            this.indexedAt = indexedAt;
            return this;
        }

        public Builder viewCount(Long viewCount) {
            this.viewCount = viewCount;
            return this;
        }

        public Document build() {
            Document doc = new Document();
            doc.title = this.title;
            doc.content = this.content;
            doc.tags = this.tags;
            doc.indexedAt = this.indexedAt != null ? this.indexedAt : LocalDateTime.now();
            doc.viewCount = this.viewCount;
            return doc;
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public LocalDateTime getIndexedAt() { return indexedAt; }
    public void setIndexedAt(LocalDateTime indexedAt) { this.indexedAt = indexedAt; }
    public Long getViewCount() { return viewCount; }
    public void setViewCount(Long viewCount) { this.viewCount = viewCount; }
}