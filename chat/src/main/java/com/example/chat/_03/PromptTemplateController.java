package com.example.chat._03;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController()
@RequestMapping("/chat/03")
public class PromptTemplateController {
    private final ChatClient chatClient;

    public PromptTemplateController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/joke")
    public String getJoke(@RequestParam(value = "topic", defaultValue = "cows") String topic){

        // Prompt template enables us to safely inject user input into the prompt
        // text in {} is replaced by the value of the variable with the same name.
        // PromptTemplate is a commonly used class with Spring AI
        PromptTemplate promptTemplate = new PromptTemplate("Tell me a joke about {topic}");
        promptTemplate.add("topic", topic);
        Prompt prompt = promptTemplate.create();

        // call the AI model and get the respnose.
        ChatResponse response = chatClient.call(prompt);
        AssistantMessage assistantMessage = response.getResult().getOutput();
        return assistantMessage.getContent();
    }
}
