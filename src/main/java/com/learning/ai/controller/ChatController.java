package com.learning.ai.controller;

import com.learning.ai.dto.ChatRequest;
import com.learning.ai.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private  final ChatService chatService;

    @GetMapping
    public String chat(){
        String message = "What is java?";
        ChatRequest chatMessage = new ChatRequest();
        chatMessage.setMessage(message);
        return chatService.chat(chatMessage);
    }

}
