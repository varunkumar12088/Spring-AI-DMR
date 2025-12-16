package com.learning.ai.config;

import com.learning.ai.properties.AIProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class GeneralConfig {

    private final AIProperties aiProperties;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(aiProperties.weather().baseUrl())
                .build();
    }

}
