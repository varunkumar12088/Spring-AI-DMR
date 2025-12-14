package com.learning.ai.service;

import com.learning.ai.dto.ChatRequest;

public interface ChatService {

   String chat(String message);

    String chat(ChatRequest chatRequest);


}
