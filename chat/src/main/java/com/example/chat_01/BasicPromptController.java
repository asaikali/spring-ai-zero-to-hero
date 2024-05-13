package com.example.chat_01;

import org.springframework.ai.chat.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat/01")
public class BasicPromptController {

    // ChatClient is the primary interfaces for interacting with an LLM
    // it is a request/response interface that implements the ModelClient
    // interface. Make suer to visit the source code of the ChatClient and
    // checkout the interfaces in the core spring ai package.
    private final ChatClient chatClient;

    public BasicPromptController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("joke")
    public String getJoke() {
        return chatClient.call("Tell me a joke");
    }
}
