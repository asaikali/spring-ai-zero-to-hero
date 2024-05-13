package com.example.embed_01;

import org.springframework.ai.chat.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/embed/01")
public class BasicEmbeddingController {

    private final ChatClient chatClient;

    public BasicEmbeddingController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("joke")
    public String getJoke() {
        return chatClient.call("Tell me a joke");
    }
}
