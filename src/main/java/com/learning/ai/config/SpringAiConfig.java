package com.learning.ai.config;

import com.learning.ai.properties.AIProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.mongodb.atlas.MongoDBAtlasVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Slf4j
@Configuration
public class SpringAiConfig {

    private final AIProperties aiProperties;

    public SpringAiConfig(AIProperties aiProperties) {
        this.aiProperties = aiProperties;
    }

    @Bean
    public OpenAiApi openAiApi() {
        log.info("Conjuring OpenAI API with base URL: {}", aiProperties.baseUrl());
        return OpenAiApi.builder()
                .apiKey(aiProperties.apiKey())
                .baseUrl(aiProperties.baseUrl())
                .build();
    }

    @Bean
    public OpenAiChatModel openAiChatMode(OpenAiApi openAiApi){
        return OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(aiProperties.modelName())
                        .temperature(aiProperties.temperature())
                        .topP(aiProperties.topP())
                        .maxTokens(aiProperties.maxTokens())
                        .build())
                .build();
    }

    @Bean
    public ChatClient chatClient(OpenAiChatModel chatModel) {
        log.info("Initializing ChatClient with model: {}", aiProperties.modelName());
        return ChatClient.builder(chatModel)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(aiProperties.modelName())
                        .temperature(aiProperties.temperature())
                        .topP(aiProperties.topP())
                        .maxTokens(aiProperties.maxTokens())
                        .build())
                .build();
    }

/*    @Bean
    public EmbeddingModel embeddingModel(OpenAiApi openAiApi) {
        log.info("Creating OpenAiEmbeddingModel");

        return OpenAiEmbeddingModel.builder()
                .api(openAiApi)
                .options(OpenAiEmbeddingOptions.builder()
                        .model("text-embedding-3-small")
                        .build())
                .build();
    }*/

    @Bean
    public EmbeddingModel embeddingModel(OpenAiApi openAiApi) {
        log.info("Initializing Embedding Model");
        return new OpenAiEmbeddingModel(openAiApi);
    }

    @Bean("customVectorStore")
    public VectorStore vectorStore(MongoTemplate mongoTemplate, EmbeddingModel embeddingModel) {
        log.info("Initializing MongoDB Vector Store");
        return MongoDBAtlasVectorStore.builder(mongoTemplate, embeddingModel)
                .collectionName(aiProperties.vectorStore().collectionName())
                .vectorIndexName(aiProperties.vectorStore().indexName())
                .pathName(aiProperties.vectorStore().pathName())
                .metadataFieldsToFilter(aiProperties.vectorStore().metadata())
                .build();
    }
}
