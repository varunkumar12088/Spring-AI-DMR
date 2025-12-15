package com.learning.ai.service;

import org.springframework.ai.document.Document;

import java.util.List;

public interface DataLoaderService {

    List<Document> loadDocumentsFromJson();

    List<Document> loadDocumentsFromPdf();

}
