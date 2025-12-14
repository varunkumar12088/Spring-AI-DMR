package com.learning.ai.repository;

import com.learning.ai.model.ConversationHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationHistoryRepository extends MongoRepository<ConversationHistory,String> {

    Optional<ConversationHistory> findByConversationId(String conversationId);

    Optional<ConversationHistory> findByConversationIdAndUserId(String conversationId, String userId);
}
