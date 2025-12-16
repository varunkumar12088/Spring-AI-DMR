package com.learning.ai.controller;

import com.learning.ai.dto.ChatRequest;
import com.learning.ai.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping(value = "/question")
    public String chat(@RequestParam(name = "q") String question){
        return chatService.chat(question);
    }

    @GetMapping(value = "/question-answer")
    public String chatQuestionAnswer(@RequestParam(name = "q") String question){
        ChatRequest chatMessage = new ChatRequest();
        chatMessage.setMessage(question);
        return chatService.chatQuestionAnswer(chatMessage);
    }

    @GetMapping(value = "/question/rag-pipeline")
    public String chatRagPipeline(@RequestParam(name = "q") String question){
        ChatRequest chatMessage = new ChatRequest();
        chatMessage.setMessage(question);
        return chatService.chatRagPipeline(chatMessage);
    }

    @GetMapping(value = "/question-tool")
    public String chatTool(@RequestParam(name = "q") String question){
        ChatRequest chatMessage = new ChatRequest();
        chatMessage.setMessage(question);
        return chatService.chatTool(chatMessage);
    }

}
