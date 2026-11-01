package com.search;

import com.search.model.Document;
import com.search.repository.DocumentRepository;
import com.search.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {

    @Mock
    private DocumentRepository repository;

    @InjectMocks
    private DocumentService documentService;

    @Test
    public void testBuilderPattern() {
        Document doc = new Document.Builder()
                .title("Elasticsearch Guide")
                .content("Learn Elasticsearch full-text search")
                .tags("search, elastic")
                .indexedAt(LocalDateTime.now())
                .viewCount(0L)
                .build();
        assertNotNull(doc);
        assertEquals("Elasticsearch Guide", doc.getTitle());
        System.out.println("Builder Pattern Test Passed: Elasticsearch Guide");
    }

    @Test
    public void testDocumentEntity() {
        Document doc = new Document();
        doc.setTitle("Test Doc");
        doc.setContent("Test content for search");
        doc.setTags("java, search");
        doc.setViewCount(0L);
        assertEquals("Test Doc", doc.getTitle());
        assertEquals(0L, doc.getViewCount());
        System.out.println("Document Entity Test Passed: Test Doc");
    }

    @Test
    public void testFormatDocumentInfo() {
        Document doc = new Document.Builder()
                .title("Test Doc")
                .content("Content")
                .tags("test")
                .indexedAt(LocalDateTime.now())
                .viewCount(5L)
                .build();
        String info = documentService.formatDocumentInfo(doc);
        assertNotNull(info);
        assertTrue(info.contains("Test Doc"));
        System.out.println("Format Document Test Passed: " + info);
    }

    @Test
    public void testGetDocumentById_Mockito() {
        Document doc = new Document.Builder().title("Mock Doc").content("Mock content").tags("mock").build();
        doc.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(doc));
        Document result = documentService.getDocumentById(1L);
        assertNotNull(result);
        assertEquals("Mock Doc", result.getTitle());
        verify(repository, times(1)).findById(1L);
        System.out.println("Mockito Test Passed: getDocumentById - Mocked Repo - Found: Mock Doc");
    }

    @Test
    public void testGetAllDocuments_Mockito() {
        Document d1 = new Document.Builder().title("Doc1").content("Content1").tags("tag1").build();
        Document d2 = new Document.Builder().title("Doc2").content("Content2").tags("tag2").build();
        when(repository.findAllByOrderByIndexedAtDesc()).thenReturn(Arrays.asList(d1, d2));
        List<Document> all = documentService.getAllDocuments();
        assertEquals(2, all.size());
        System.out.println("Mockito Test Passed: getAllDocuments - Mocked 2 docs - Caching: documents");
    }

    @Test
    public void testSearchDocuments_Mockito() {
        Document doc = new Document.Builder().title("Java Search").content("Elasticsearch pattern").tags("java").build();
        when(repository.findAll()).thenReturn(Arrays.asList(doc));
        List<Document> results = documentService.search("Java", "exact");
        assertNotNull(results);
        assertEquals(1, results.size());
        System.out.println("Search Test Passed: Found " + results.size() + " doc for keyword Java - Strategy Pattern Exact");
    }
}