package com.search.repository;

import com.search.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findAllByOrderByIndexedAtDesc();
    List<Document> findByTitleContainingIgnoreCase(String title);
    List<Document> findByContentContainingIgnoreCase(String content);
    List<Document> findByTagsContainingIgnoreCase(String tags);
}