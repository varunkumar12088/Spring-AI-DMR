package com.learning.ai.config;

import com.learning.ai.properties.AIProperties;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Slf4j
@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    private final AIProperties properties;

    public MongoConfig(AIProperties properties) {
        this.properties = properties;
    }

    @Override
    protected String getDatabaseName() {
        return this.properties.databaseName();
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        String username = System.getenv("MONGO_USERNAME");
        String password = System.getenv("MONGO_PASSWORD");
        String host = System.getenv("MONGO_HOST");

        if (StringUtils.isAnyBlank(username, password, host)) {
            System.out.println("MongoDB connection details are not set in environment variables.");
            throw new IllegalStateException("MongoDB connection details are not set.");
        }

        String connectionString = String.format(
                "mongodb+srv://%s:%s@%s/%s?retryWrites=true&w=majority",
                username, password, host, this.properties.databaseName()
        );
        System.out.println("Connecting to MongoDB: " + connectionString);
        return MongoClients.create(connectionString);
    }


}
