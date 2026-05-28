package com.example.mem_03;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Demonstrates MessageChatMemoryAdvisor which injects conversation history as structured messages.
 * This preserves message roles (user/assistant) and is the recommended approach for most use cases.
 *
 * <p>Compare with mem_02 which uses PromptChatMemoryAdvisor that appends history as plain text.
 */
@RestController
@RequestMapping("/mem/03")
public class MessageMemoryController {

  private final ChatClient chatClient;
  private final MessageChatMemoryAdvisor messageChatMemoryAdvisor;

  @Autowired
  public MessageMemoryController(ChatClient.Builder builder) {
    this.chatClient = builder.build();

    // MessageWindowChatMemory maintains a sliding window of messages
    ChatMemory memory =
        MessageWindowChatMemory.builder()
            .chatMemoryRepository(new InMemoryChatMemoryRepository())
            .maxMessages(20) // Keep last 20 messages
            .build();

    // MessageChatMemoryAdvisor preserves message structure (roles, formatting)
    // This is safer than PromptChatMemoryAdvisor for most models
    this.messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(memory).build();
  }

  @GetMapping("/hello")
  public String query(
      @RequestParam(
              value = "message",
              defaultValue = "Hello my name is John, what is the capital of France?")
          String message) {

    return this.chatClient
        .prompt()
        .advisors(messageChatMemoryAdvisor)
        .user(message)
        .call()
        .content();
  }

  @GetMapping("/name")
  public String name() {
    return this.chatClient
        .prompt()
        .advisors(messageChatMemoryAdvisor)
        .user("What is my name?")
        .call()
        .content();
  }

  @GetMapping("/with-session")
  public String queryWithSession(
      @RequestParam(value = "message", defaultValue = "Hello, my name is Alice") String message,
      @RequestParam(value = "sessionId", defaultValue = "default") String sessionId) {

    // Use conversation ID to maintain separate conversation contexts
    return this.chatClient
        .prompt()
        .advisors(
            a -> a.advisors(messageChatMemoryAdvisor).param(ChatMemory.CONVERSATION_ID, sessionId))
        .user(message)
        .call()
        .content();
  }
}
