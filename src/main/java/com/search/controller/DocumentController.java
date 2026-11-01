package com.search.controller;

import com.search.model.Document;
import com.search.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> map = new HashMap<>();
        map.put("project", "97-search-with-elastic");
        map.put("status", "Live - Search with Elastic - Tier 10 Scale");
        map.put("tier", "10 - System Design Level - Caching + Security + Testing + Design Patterns + Elasticsearch");
        map.put("caching", "Caffeine Cache - documents cache - 1000 max, 600s expire - Stats - Redis ready");
        map.put("security", "X-Content-Type-Options nosniff, X-Frame-Options DENY, XSS Protection");
        map.put("designPatterns", List.of("Builder Pattern - Document.Builder", "Strategy Pattern - Exact vs Fuzzy vs Autocomplete", "Repository Pattern", "Caching Pattern"));
        map.put("elasticsearch", "Pattern Ready - Exact Search + Fuzzy Search + Autocomplete + Inverted Index - Replace with Spring Data Elasticsearch");
        map.put("port", "8099");
        map.put("testing", "JUnit 5 + Mockito - 6 tests");
        map.put("live", "https://nine7-search-with-elastic.onrender.com");
        map.put("owner", "Raviteja");
        return map;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> map = new HashMap<>();
        map.put("status", "UP");
        map.put("project", "97-search-with-elastic");
        map.put("cache", "Active - Caffeine - documents, document, searchResults - Stats enabled");
        map.put("security", "Headers Active");
        map.put("search", "Active - Elasticsearch Pattern - Exact + Fuzzy + Autocomplete - Strategy Pattern");
        map.put("githubGoal", "CONTRIBUTING.md + Issues + Release Tags + Badges + Live Link + Architecture Diagram - Tier 10 Goal");
        return map;
    }

    // Index - Builder Pattern + CacheEvict - Same as 96 upload
    @PostMapping("/api/search/index")
    public ResponseEntity<Map<String, Object>> indexDocument(@RequestParam String title, @RequestParam String content, @RequestParam(required = false) String tags) {
        Document doc = documentService.indexDocument(title, content, tags != null ? tags : "general");
        Map<String, Object> res = new HashMap<>();
        res.put("id", doc.getId());
        res.put("title", doc.getTitle());
        res.put("content", doc.getContent().substring(0, Math.min(100, doc.getContent().length())) + "...");
        res.put("tags", doc.getTags());
        res.put("indexedAt", doc.getIndexedAt());
        res.put("viewCount", doc.getViewCount());
        res.put("message", "Indexed - Search Ready - Cache Evicted - Builder Pattern - Elasticsearch Pattern");
        res.put("cache", "documents cache evicted");
        res.put("searchUrl", "/api/search?q=" + title);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/api/documents")
    public ResponseEntity<List<Document>> getAllDocuments() {
        List<Document> docs = documentService.getAllDocuments();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Cache", "Caffeine - documents");
        headers.add("X-Search-Engine", "Elasticsearch-Pattern-Ready");
        return ResponseEntity.ok().headers(headers).body(docs);
    }

    @GetMapping("/api/documents/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable Long id) {
        Document doc = documentService.getDocumentById(id);
        if (doc == null) return ResponseEntity.notFound().build();
        doc.setViewCount(doc.getViewCount() + 1);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Cache", "Caffeine - document");
        return ResponseEntity.ok().headers(headers).body(doc);
    }

    // Search - Strategy Pattern - Exact Strategy - Elasticsearch match query
    @GetMapping("/api/search")
    public ResponseEntity<Map<String, Object>> search(@RequestParam String q, @RequestParam(required = false, defaultValue = "exact") String strategy) {
        List<Document> results = documentService.search(q, "exact");
        Map<String, Object> res = new HashMap<>();
        res.put("query", q);
        res.put("strategy", "Exact-Strategy");
        res.put("results", results);
        res.put("count", results.size());
        res.put("engine", "Elasticsearch-Pattern - match query");
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Search-Strategy", "Exact-Strategy");
        headers.add("X-Cache", "Caffeine - searchResults");
        return ResponseEntity.ok().headers(headers).body(res);
    }

    // Autocomplete - Strategy Pattern - Autocomplete Strategy - Elasticsearch edge_ngram + completion suggester
    @GetMapping("/api/search/autocomplete")
    public ResponseEntity<Map<String, Object>> autocomplete(@RequestParam String prefix) {
        List<Document> results = documentService.search(prefix, "autocomplete");
        Map<String, Object> res = new HashMap<>();
        res.put("prefix", prefix);
        res.put("strategy", "Autocomplete-Strategy");
        res.put("results", results);
        res.put("count", results.size());
        res.put("engine", "Elasticsearch-Pattern - edge_ngram + completion suggester");
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Search-Strategy", "Autocomplete-Strategy");
        return ResponseEntity.ok().headers(headers).body(res);
    }

    // Fuzzy - Strategy Pattern - Fuzzy Strategy - Elasticsearch fuzziness
    @GetMapping("/api/search/fuzzy")
    public ResponseEntity<Map<String, Object>> fuzzySearch(@RequestParam String q) {
        List<Document> results = documentService.search(q, "fuzzy");
        Map<String, Object> res = new HashMap<>();
        res.put("query", q);
        res.put("strategy", "Fuzzy-Strategy");
        res.put("results", results);
        res.put("count", results.size());
        res.put("engine", "Elasticsearch-Pattern - fuzziness AUTO");
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Search-Strategy", "Fuzzy-Strategy");
        return ResponseEntity.ok().headers(headers).body(res);
    }
}