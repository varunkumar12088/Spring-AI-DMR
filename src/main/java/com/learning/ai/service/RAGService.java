package com.learning.ai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RAGService {

    private final VectorStore vectorStore;
    private final ChatService chatService;
    public RAGService(@Qualifier("customVectorStore") VectorStore vectorStore, ChatService chatService) {
        this.vectorStore = vectorStore;
        this.chatService = chatService;
    }

    public void addDocuments(List<String> documents) {
        List<Document> documentList =documents.stream().map(Document::new)
                .toList();
        vectorStore.add(documentList);
        log.info("Adding documents to the vector store: {}", documentList.stream());
    }

    public List<Document> searchSimilar(String query, int toPk, double similarity) {
        similarity = similarity > 0 ? similarity : 0.8;
        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(toPk)
                .similarityThreshold(similarity)
                .build();
        return vectorStore.similaritySearch(searchRequest);
    }

    public String ragChat(String conversationId, String query, String systemPrompt) {
        log.info("RAG Chat for Conversation ID: {}", conversationId);
        // Search for relevant documents
        List<Document> documents = searchSimilar(query, 3, 0.5);

        // Build context from similar documents
        StringBuilder context = new StringBuilder();
        context.append("Retrieved Context:\n");
        for (Document doc : documents) {
            context.append("- ").append(doc.getText()).append("\n");
        }

        // Enhance system prompt with context
        String enhancedPrompt = (systemPrompt != null ? systemPrompt : "")
                + "\n\n" + context
                + "\n\nUse the above context to answer the user's question accurately.";

        // Send to chat service with enhanced prompt
        return chatService.chat(conversationId, query, enhancedPrompt);
    }
}
