package com.learning.ai.config;

import com.learning.ai.properties.AIProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AIConfig {

    private final ChatModel chatModel;
    private final AIProperties  aiProperties;

    @Bean
    public ChatClient chatClient() {
        return ChatClient.builder(chatModel)
                .defaultOptions(ChatOptions.builder()
                        .model(aiProperties.modelName())
                        .temperature(aiProperties.temperature())
                        .topP(aiProperties.topP())
                        .build())
                .build();
    }
}
