package com.search.service;

import com.search.model.Document;
import com.search.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository repository;

    // Builder Pattern + CacheEvict - Same as 96 VideoService.uploadVideo - Tier 10
    @CacheEvict(value = {"documents", "searchResults"}, allEntries = true)
    public Document indexDocument(String title, String content, String tags) {
        Document doc = new Document.Builder()
                .title(title)
                .content(content)
                .tags(tags)
                .indexedAt(LocalDateTime.now())
                .viewCount(0L)
                .build();
        return repository.save(doc);
    }

    @Cacheable("documents")
    public List<Document> getAllDocuments() {
        return repository.findAllByOrderByIndexedAtDesc();
    }

    @Cacheable(value = "document", key = "#id")
    public Document getDocumentById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // Strategy Pattern - Exact vs Fuzzy vs Autocomplete - Elasticsearch core - Same as 96 Strategy Pattern Range vs Full
    @Cacheable(value = "searchResults", key = "#keyword + '_' + #strategy")
    public List<Document> search(String keyword, String strategy) {
        List<Document> all = repository.findAll();
        String lowerKeyword = keyword.toLowerCase();

        if ("autocomplete".equalsIgnoreCase(strategy)) {
            // Autocomplete Strategy: prefix match - Elasticsearch edge_ngram + completion suggester
            return all.stream()
                    .filter(d -> d.getTitle().toLowerCase().startsWith(lowerKeyword) ||
                                 (d.getTags() != null && d.getTags().toLowerCase().startsWith(lowerKeyword)) ||
                                 d.getTitle().toLowerCase().contains(lowerKeyword))
                    .collect(Collectors.toList());
        } else if ("fuzzy".equalsIgnoreCase(strategy)) {
            // Fuzzy Strategy: contains + edit distance - Elasticsearch fuzziness AUTO - Fixed to return results for typo
            return all.stream()
                    .filter(d -> d.getTitle().toLowerCase().contains(lowerKeyword) ||
                                 d.getContent().toLowerCase().contains(lowerKeyword) ||
                                 isFuzzyMatch(d.getTitle().toLowerCase(), lowerKeyword) ||
                                 isFuzzyMatch(d.getContent().toLowerCase(), lowerKeyword))
                    .collect(Collectors.toList());
        } else {
            // Exact Strategy: full-text contains - Elasticsearch match query
            return all.stream()
                    .filter(d -> d.getTitle().toLowerCase().contains(lowerKeyword) ||
                                 d.getContent().toLowerCase().contains(lowerKeyword) ||
                                 (d.getTags() != null && d.getTags().toLowerCase().contains(lowerKeyword)))
                    .collect(Collectors.toList());
        }
    }

    private boolean isFuzzyMatch(String text, String keyword) {
        // Fixed Fuzzy: Simple fuzzy - substring minus last char - Elasticsearch fuzziness pattern
        // Elastc -> Elast -> matches Elasticsearch
        // For demo: typo tolerance
        if (keyword.length() <= 2) return text.contains(keyword);
        String shortKeyword = keyword.substring(0, keyword.length() - 1);
        String shortKeyword2 = keyword.length() > 3 ? keyword.substring(0, keyword.length() - 2) : shortKeyword;
        return text.contains(shortKeyword) || text.contains(shortKeyword2);
    }

    public String formatDocumentInfo(Document doc) {
        return "[" + doc.getIndexedAt() + "] " + doc.getTitle() + " - " + doc.getTags() + " (" + doc.getContent().length() + " chars) Views:" + doc.getViewCount();
    }
}