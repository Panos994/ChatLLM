package com.myorg.chatllm.service;


import com.myorg.chatllm.entity.ChatMessage;
import com.myorg.chatllm.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessage processUserMessage(String userMessage) {
        // Save user message
        ChatMessage userMsg = ChatMessage.builder()
                .sender("USER")
                .message(userMessage)
                .build();
        chatMessageRepository.save(userMsg);

        // Generate bot response (dummy logic for now)
        String botResponse = generateSimpleResponse(userMessage);

        // Save bot message
        ChatMessage botMsg = ChatMessage.builder()
                .sender("BOT")
                .message(botResponse)
                .build();
        chatMessageRepository.save(botMsg);

        return botMsg;
    }

    // ---------------------------
    // Simple rule-based "LLM core"
    // ---------------------------
    private String generateSimpleResponse(String input) {
        input = input.toLowerCase();
        if (input.contains("hello") || input.contains("hi")) {
            return "Hello! How can I help you today?";
        } else if (input.contains("how are you")) {
            return "I'm just code, but I'm doing great! 😊";
        } else {
            return "I didn't quite understand that, but I'm learning!";
        }
    }
}