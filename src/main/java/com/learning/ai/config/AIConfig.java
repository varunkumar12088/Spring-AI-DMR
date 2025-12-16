package com.learning.ai.config;

import com.learning.ai.properties.AIProperties;
import com.learning.ai.tools.SimpleDateTimeTool;
import com.learning.ai.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AIConfig {

    private final ChatModel chatModel;
    private final AIProperties  aiProperties;

    @Bean
    public ChatClient chatClient() {
        LogUtil.log(this.aiProperties);
        return ChatClient.builder(chatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor(),
                        new SafeGuardAdvisor(List.of("games")))
                .defaultOptions(ChatOptions.builder()
                        .model(aiProperties.modelName())
                        .temperature(aiProperties.temperature())
                        .topP(aiProperties.topP())
                        .maxTokens(aiProperties.maxTokens())
                        .build())
                .defaultTools(new SimpleDateTimeTool())
                .build();
    }
}
