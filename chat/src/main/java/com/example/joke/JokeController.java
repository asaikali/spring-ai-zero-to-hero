package com.example.joke;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class JokeController {
    private final ChatClient chatClient;

    public JokeController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/joke")
    public String getJoke() {
        return chatClient.call("Tell me a joke");
    }

    @GetMapping("/joke/about")
    public String getJoke(@RequestParam(value = "topic", defaultValue = "cows") String topic){
        PromptTemplate promptTemplate = new PromptTemplate("Tell me a joke about {topic}");
        promptTemplate.add("topic", topic);
        Prompt prompt = promptTemplate.create();
        ChatResponse response = chatClient.call(prompt);
        AssistantMessage assistantMessage = response.getResult().getOutput();
        return assistantMessage.getContent();
    }
}
