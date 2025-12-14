package com.learning.ai.service.impl;

import com.learning.ai.dto.ChatRequest;
import com.learning.ai.helper.Helper;
import com.learning.ai.model.ChatMessage;
import com.learning.ai.repository.ChatMessageRepository;
import com.learning.ai.service.ChatService;
import com.learning.ai.service.VectorDocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;
    private final ChatMessageRepository chatMessageRepository;
    private final VectorDocumentService  vectorDocumentService;

    @Override
    public String chat(String message) {
        List<Document> documents = vectorDocumentService.search(message);
        String context = buildContext(documents);
        String systemMessage = Helper.systemMessage(context);
        String userMessage = Helper.userMessage(message);
        String response = this.chatClient.prompt()
                .advisors(new SimpleLoggerAdvisor())
                .system(systemMessage)
                .user(userMessage)
                .call()
                .content();
        return response;
    }

    @Override
    public String chat(ChatRequest chatRequest) {
        log.info("message:{}",chatRequest.getMessage());
        saveChatMessage(chatRequest);
        List<Message> messages = new ArrayList<>();
        messages.add(new UserMessage(chatRequest.getMessage()));
        Prompt prompt = new Prompt(messages);
        String response = chatClient.prompt(prompt).call().content();
        return response;
    }

    private void saveChatMessage(ChatRequest chatRequest) {
        ChatMessage chatMessage = ChatMessage.builder()
                .content(chatRequest.getMessage())
                .userId(chatRequest.getUserId() == null ? "DEFAULT" : chatRequest.getUserId())
                .conversationId(chatRequest.getConversationId() == null ? "DEFAULT" : chatRequest.getConversationId())
                .role("user")
                .timestamp(Instant.now())
                .build();
        chatMessageRepository.save(chatMessage);
    }

    private String buildContext(List<Document> documents){
        return documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining(","));
    }
}
