package com.learning.ai.service;

import org.springframework.ai.document.Document;

import java.util.List;

public interface VectorDocumentService {

    void addDocuments(List<String> texts);

    void addDocumentsFromJson();

    void addDocumentsFromPdf();

    List<Document> search(String query);
}
