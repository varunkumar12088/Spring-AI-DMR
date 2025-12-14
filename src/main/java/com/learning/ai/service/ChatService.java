package com.learning.ai.service;

import com.learning.ai.model.ChatMessage;
import com.learning.ai.model.ConversationHistory;
import com.learning.ai.repository.ChatMessageRepository;
import com.learning.ai.repository.ConversationHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ChatService {

    private final ChatClient chatClient;
    private final ChatMessageRepository  chatMessageRepository;
    private final ConversationHistoryRepository  conversationHistoryRepository;

    public ChatService(ChatClient  chatClient,
                       ChatMessageRepository chatMessageRepository,
                       ConversationHistoryRepository conversationHistoryRepository) {
        this.chatClient = chatClient;
        this.chatMessageRepository = chatMessageRepository;
        this.conversationHistoryRepository = conversationHistoryRepository;
    }

    public String createConversation(String userId) {
        String conversationId = UUID.randomUUID().toString();
        ConversationHistory  conversationHistory = ConversationHistory.builder()
                .conversationId(conversationId)
                .userId(userId)
                .messages(new ArrayList<>())
                .build();
        conversationHistoryRepository.save(conversationHistory);
        log.info("Created conversation: {} for user: {}", conversationId, userId);
        return conversationId;
    }

    public String chat(String conversationId, String userMessage, String systemPrompt) {
        // Get conversation history
        Optional<ConversationHistory> conversationOpt = conversationHistoryRepository.findByConversationId(conversationId);
        if(conversationOpt.isEmpty()) {
            throw new IllegalStateException("Conversation with id " + conversationId + " does not exist");
        }
        ConversationHistory conversationHistory = conversationOpt.get();
        // save user message
        ChatMessage chatMessage = saveChatMessage(conversationId, userMessage);
        conversationHistory.addMessage(chatMessage);

        // Messages
        List<Message> messages = createMessageList(conversationId, systemPrompt);

        // Get response from model
        Prompt prompt = new Prompt(messages);
        String response = chatClient.prompt(prompt).call().content();

        log.info("Received response from model for conversation: {}", conversationId);
        saveConversationHistory(conversationHistory, conversationId, response);

        return response;
    }

    public List<ChatMessage> getConversationHistoryChatMessages(String conversationId) {
        return chatMessageRepository.findByConversationId(conversationId);
    }

    public void clearConversationHistory(String conversationId) {
        chatMessageRepository.deleteByConversationId(conversationId);
        conversationHistoryRepository.deleteById(conversationId);
    }

    private ChatMessage saveChatMessage(String conversationId, String userMessage) {
        ChatMessage chatMessage = ChatMessage.builder()
                .conversationId(conversationId)
                .role("user")
                .content(userMessage)
                .timestamp(LocalDateTime.now())
                .build();
        return chatMessageRepository.save(chatMessage);

    }

    private List<Message> createMessageList(String conversationId, String systemPrompt) {
        List<Message> messages = new ArrayList<>();
        // Add system prompt
        if (StringUtils.isNoneBlank(systemPrompt)) {
            messages.add(new SystemMessage(systemPrompt));
        }
        // Add chat history (last 10 messages for context)
        List<ChatMessage> recentMessages = chatMessageRepository.findByConversationId(conversationId);
        int startIdx = Math.max(0, recentMessages.size() - 10);
        for (int i = startIdx; i < recentMessages.size(); i++) {
            ChatMessage msg = recentMessages.get(i);
            if ("user".equals(msg.getRole())) {
                messages.add(new UserMessage(msg.getContent()));
            } else {
                messages.add(new AssistantMessage(msg.getContent()));
            }
        }
        return  messages;
    }

    private void saveConversationHistory(ConversationHistory conversationHistory,
                                         String conversationId,
                                         String response) {
        // Save assistant response
        ChatMessage assistantMsg = ChatMessage.builder()
                .conversationId(conversationId)
                .role("assistant")
                .content(response)
                .timestamp(LocalDateTime.now())
                .build();
        chatMessageRepository.save(assistantMsg);
        conversationHistory.addMessage(assistantMsg);
        // Update conversation
        conversationHistoryRepository.save(conversationHistory);
    }
}
