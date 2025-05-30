package com.example.mem_02;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mem/02")
public class ChatHistoryController {

  private final ChatClient chatClient;
  private final PromptChatMemoryAdvisor promptChatMemoryAdvisor;

  @Autowired
  public ChatHistoryController(ChatClient.Builder builder) {
    this.chatClient = builder.build();

    var memory =
        MessageWindowChatMemory.builder()
            .chatMemoryRepository(new InMemoryChatMemoryRepository())
            .build();
    this.promptChatMemoryAdvisor = PromptChatMemoryAdvisor.builder(memory).build();
  }

  @GetMapping("/hello")
  public String query(
      @RequestParam(
              value = "message",
              defaultValue = "Hello my name is John, what is the capital of France?")
          String message) {

    return this.chatClient
        .prompt()
        .advisors(promptChatMemoryAdvisor)
        .user(message)
        .call()
        .content();
  }

  @GetMapping("/name")
  public String name() {
    return this.chatClient
        .prompt()
        .advisors(promptChatMemoryAdvisor)
        .user("What is my name?")
        .call()
        .content();
  }
}
