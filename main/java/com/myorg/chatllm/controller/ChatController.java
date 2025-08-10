package com.myorg.chatllm.controller;


import com.myorg.chatllm.entity.ChatMessage;
import com.myorg.chatllm.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/send")
    public ResponseEntity<ChatMessage> sendMessage(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        ChatMessage botResponse = chatService.processUserMessage(userMessage);
        return ResponseEntity.ok(botResponse);
    }
}
