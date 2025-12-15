package com.learning.ai.service.impl;

import com.learning.ai.service.DataLoaderService;
import com.learning.ai.service.DataTransformerService;
import com.learning.ai.service.VectorDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VectorDocumentServiceImpl implements VectorDocumentService {

    private final VectorStore vectorStore;
    private final DataLoaderService  dataLoaderService;
    private final DataTransformerService dataTransformerService;

    @Override
    public void addDocuments(List<String> texts) {
        List<Document> documents = texts.stream()
                .map(text -> new Document(text))
                .toList();

        vectorStore.add(documents);
    }

    @Override
    public void addDocumentsFromJson() {
        List<Document> documents = dataLoaderService.loadDocumentsFromJson();
        List<Document> documentList = dataTransformerService.transform(documents);
        vectorStore.add(documentList);
    }

    @Override
    public void addDocumentsFromPdf() {
        List<Document> documents = dataLoaderService.loadDocumentsFromPdf();
        List<Document> documentList = dataTransformerService.transform(documents);
        vectorStore.add(documentList);
    }

    @Override
    public List<Document> search(String query) {
        SearchRequest searchRequest = SearchRequest.builder()
                .similarityThreshold(0.5)
                .topK(5)
                .query(query)
                .build();
        List<Document> documents = vectorStore.similaritySearch(searchRequest);
       /* if(CollectionUtils.isEmpty(documents)) {
            documents.add(new Document("This query is out of box."));
        }*/
        return documents;
    }
}
