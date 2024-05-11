package com.example;

import org.springframework.ai.chat.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class JokeService {
    private final ChatClient chatClient;

    public JokeService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String getJoke() {
        return chatClient.call("Tell me a joke");
    }
}
