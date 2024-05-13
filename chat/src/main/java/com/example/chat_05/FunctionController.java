package com.example.chat_05;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/chat/05")
class FunctionController {
    private final ChatClient chatClient;

    public FunctionController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/weather")
    public String getWeather(@RequestParam(value = "city", defaultValue = "Toronto") String city){

        PromptTemplate promptTemplate = new PromptTemplate("""
                What is the current weather in {city}? 
                """);
        promptTemplate.add("city", city);
        Prompt prompt = promptTemplate.create();

        // call the AI model and get the respnose.
        ChatResponse response = chatClient.call(prompt);
        AssistantMessage assistantMessage = response.getResult().getOutput();
        return assistantMessage.getContent();
    }

    @GetMapping("/pack")
    public String getClothingRecommendation(@RequestParam(value = "city", defaultValue = "Toronto") String city){

        PromptTemplate promptTemplate = new PromptTemplate("""
                I am travelling to {city} what kind of cloths should I pack? 
                """);
        promptTemplate.add("city", city);
        Prompt prompt = promptTemplate.create();

        // call the AI model and get the respnose.
        ChatResponse response = chatClient.call(prompt);
        AssistantMessage assistantMessage = response.getResult().getOutput();
        return assistantMessage.getContent();
    }
}
