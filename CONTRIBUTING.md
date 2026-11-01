# Contributing to 97-search-with-elastic

Thank you for considering contributing to Project 97 - Search with Elastic - Tier 10 SCALE!

## Project Overview
Project 97 is part of Java 100 Projects Challenge - Tier 10 System Design Level - SCALE PROJECT.

Tech Stack: Java 17, Spring Boot 3.2.5, Spring Cache Caffeine (maximumSize=1000 expireAfterAccess=600s recordStats), Security Headers (nosniff DENY XSS), Design Patterns (Builder Pattern Document.Builder + Strategy Pattern Exact vs Fuzzy vs Autocomplete + Repository + Caching Pattern), Spring Data JPA, H2 Database searchdb + view_count + idx_title + idx_content, JUnit 5 + Mockito 6 tests Green Bar, Docker Multi-Stage, Maven, Tomcat 10.1.20.

## How to Run Locally
1. Clone repo: git clone https://github.com/raviteja-dev950/97-search-with-elastic.git
2. Delete META-INF if exists - Eclipse bug
3. Maven Update Project Force Update
4. Run SearchWithElasticApplication.java - Tomcat 8099 - Started in 13.463s
5. Test: GET / -> Live JSON Tier 10, GET /health -> UP, POST /api/search/index?title=...&content=...&tags=..., GET /api/documents, GET /api/search?q=..., GET /api/search/autocomplete?prefix=..., GET /api/search/fuzzy?q=...

## Bug Fixes (Same as 93/94/95/96)
- Cannot load driver class: org.h2.Driver - H2 scope runtime - Remove <scope>runtime</scope> + Maven Update
- META-INF folder bug - Eclipse creates src/main/resources/META-INF breaking Maven - Delete META-INF + Clean + Update
- docker.io/library/openjdk:17-jdk-slim not found - Use eclipse-temurin:17-jdk-alpine
- COPY target/*.jar ERROR lstat /target no such file - Use Multi-Stage Dockerfile maven:3.9-eclipse-temurin-17 AS build + COPY --from=build
- Was 2/2 tests Tier 5 - Not Tier 10 - Rebuilt to 6/6 Tier 10 SCALE - Builder + Entity + Format + Mockito getById + getAll + Search
- Fuzzy search count 0 - Fixed isFuzzyMatch logic - Elastc -> Elast -> matches Elasticsearch - Elasticsearch fuzziness AUTO

## Features
- Caching: CacheConfig.java @Configuration @EnableCaching + CaffeineCacheManager maximumSize=1000 expireAfterAccess=600s recordStats() + @Cacheable("documents") + @Cacheable("document") + @Cacheable("searchResults") + @CacheEvict(allEntries=true) + X-Cache: Caffeine + Redis pattern ready
- Security: SecurityHeadersConfig.java @Configuration implements WebMvcConfigurer + HandlerInterceptor + X-Content-Type-Options nosniff + X-Frame-Options DENY + X-XSS-Protection + Validation @NotBlank
- Builder Pattern: Document.java inner Builder title() content() tags() indexedAt() viewCount() build() Fluent API - No telescoping constructor
- Strategy Pattern: DocumentService.search() - Exact vs Fuzzy vs Autocomplete - Range: q=Java -> Exact-Strategy - prefix=Ja -> Autocomplete-Strategy edge_ngram + completion suggester - q=Elastc typo -> Fuzzy-Strategy fuzziness AUTO - Elasticsearch core
- Repository Pattern: DocumentRepository extends JpaRepository<Document, Long> + findAllByOrderByIndexedAtDesc + findByTitleContainingIgnoreCase + findByContentContainingIgnoreCase + findByTagsContainingIgnoreCase + idx_title + idx_content
- Caching Pattern: Cache-aside - Stats enabled - Redis ready
- Elasticsearch Pattern: Full-text search + Autocomplete + Fuzzy search + Indexing - In-memory inverted index - Replace 1 line with Spring Data Elasticsearch for real ES
- JUnit 5 + Mockito: DocumentServiceTest 6 tests green - testBuilderPattern + testDocumentEntity + testFormatDocumentInfo + testGetDocumentById_Mockito + testGetAllDocuments_Mockito + testSearchDocuments_Mockito - Runs 6/6 Errors 0 Failures 0 Green Bar 5.787s

## 【entity-GitHub¦canonical_name=GitHub】 Goal - Tier 10
- CONTRIBUTING.md 12466+ chars
- .【entity-github¦canonical_name=GitHub】/ISSUE_TEMPLATE/bug_report.md + feature_request.md
- Release Tag v1.0.0 + Latest badge
- 18 Badges + Live Link + Architecture Diagram + Deployed Link

## Live URLs
- Live: https://nine7-search-with-elastic.onrender.com
- Health: https://nine7-search-with-elastic.onrender.com/health
- 【entity-GitHub¦canonical_name=GitHub】: https://github.com/raviteja-dev950/97-search-with-elastic

## Roadmap
93 Rate Limited API completed, 94 URL Shortener Scale Live 2m32s, 95 Chat App WebSockets Live 2m23s, 96 Video Streaming API Live 2m23s Release v1.0.0 Latest, 97 Search with Elastic Live 2m23s THIS PROJECT - 97/102 95% - Only 5 left to 100% - 98 Recommendation Engine, 99 Multi-tenant SaaS, 100 Audit Log System, 101 Your SaaS Idea, 102 Capstone OSS Tool

Thank you for contributing! Follow roadmap - No deviation - 100 Projects Java Full Stack + 【entity-GitHub¦canonical_name=GitHub】 Profile