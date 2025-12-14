package com.learning.ai.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("ai")
public record AIProperties(
        String baseUrl,
        String databaseName,
        String modelName,
        String apiKey,
        Double temperature,
        Double topP,
        Integer maxTokens,
        VectorStore vectorStore
) {
    public record VectorStore(
            String collectionName,
            String indexName,
            String pathName,
            List<String> metadata
    ){
    }
}
