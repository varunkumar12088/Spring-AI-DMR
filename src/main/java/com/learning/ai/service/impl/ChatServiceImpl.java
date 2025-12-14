package com.learning.ai.service.impl;

import com.learning.ai.dto.ChatRequest;
import com.learning.ai.model.ChatMessage;
import com.learning.ai.repository.ChatMessageRepository;
import com.learning.ai.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public String chat(String message) {
        List<Message> messages = new ArrayList<>();
        messages.add(new UserMessage(message));
        Prompt prompt = new Prompt(messages);
        String response = chatClient.prompt(prompt).call().content();
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
}
