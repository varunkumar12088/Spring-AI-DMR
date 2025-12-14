package com.learning.ai.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequest {

    private String message;
    private String conversationId;
    private String userId;
}
