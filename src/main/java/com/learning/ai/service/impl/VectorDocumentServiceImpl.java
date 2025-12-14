package com.learning.ai.service.impl;

import com.learning.ai.service.VectorDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VectorDocumentServiceImpl implements VectorDocumentService {

    private final VectorStore vectorStore;


    @Override
    public void addDocuments(List<String> texts) {
        List<Document> documents = texts.stream()
                .map(text -> new Document(text))
                .toList();

        vectorStore.add(documents);
    }

    @Override
    public List<Document> search(String query) {
        SearchRequest searchRequest = SearchRequest.builder()
                .similarityThreshold(0.5)
                .topK(3)
                .query(query)
                .build();
        return vectorStore.similaritySearch(searchRequest);
    }
}
