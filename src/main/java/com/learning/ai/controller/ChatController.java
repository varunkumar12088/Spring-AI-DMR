package com.learning.ai.controller;

import com.learning.ai.dto.ChatRequest;
import com.learning.ai.dto.DocumentRequest;
import com.learning.ai.service.ChatService;
import com.learning.ai.service.RAGService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;
    private final RAGService ragService;
    public ChatController(ChatService chatService, RAGService ragService) {
        this.chatService = chatService;
        this.ragService = ragService;
    }

    @PostMapping("/conversations")
    public ResponseEntity<Map<String, String>> createConversation(
            @RequestParam String userId) {
        String conversationId = chatService.createConversation(userId);
        Map<String, String> response = new HashMap<>();
        response.put("conversationId", conversationId);
        response.put("status", "created");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/message")
    public ResponseEntity<Map<String, String>> sendMessage(
            @RequestParam String conversationId,
            @RequestBody ChatRequest request) {
        String systemPrompt = "You are a java developer, act as a programmer";
        String response = chatService.chat(conversationId, request.getMessage(), systemPrompt);
        Map<String, String> result = new HashMap<>();
        result.put("conversationId", conversationId);
        result.put("response", response);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/rag")
    public ResponseEntity<Map<String, String>> ragChat(
            @RequestParam String conversationId,
            @RequestBody ChatRequest request) {
        String systemPrompt = "You are a helpful assistant that answers questions based on this";
        String response = ragService.ragChat(conversationId, request.getMessage(), systemPrompt);
                Map<String, String> result = new HashMap<>();
        result.put("conversationId", conversationId);
        result.put("response", response);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/documents")
    public ResponseEntity<Map<String, String>> addDocuments(
            @RequestBody DocumentRequest request) {
        ragService.addDocuments(request.getDocuments());
        Map<String, String> result = new HashMap<>();
        result.put("status", "documents added");
        result.put("count", String.valueOf(request.getDocuments().size()));
        return ResponseEntity.ok(result);
    }
}
