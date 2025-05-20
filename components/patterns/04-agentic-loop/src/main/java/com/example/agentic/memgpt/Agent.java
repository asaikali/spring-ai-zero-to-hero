package com.example.agentic.memgpt;

import java.util.ArrayList;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;

public class Agent {

  private final String SYSTEM_PROMPT =
      """
      You are an AI agent.

      You always respond using the `send_message` tool. You never reply directly with a message.

      When you respond:
      - Use `inner_thoughts` to write private thoughts (the user never sees this).
      - Use `message` to write what the user will see.
      - Keep your `inner_thoughts` under 50 words.

      Never skip inner thoughts. Never output anything except by calling the `send_message` tool.
      """;

  private interface Message {}
  ;

  public record UserMessage(String content) implements Message {}
  ;

  public record AgentResponse(String innerThougts, String response) implements Message {}
  ;

  private final String id;
  private final String systemPrompt;
  private final ChatClient chatClient;
  private MemoryBlock coreMemory;
  private List<Message> messages = new ArrayList<>();

  public Agent(
      ChatClient.Builder builder, String id, String systemPrompt, String initialCoreMemory) {
    this.id = id;
    this.systemPrompt = systemPrompt;
    this.coreMemory = new MemoryBlock("core", initialCoreMemory);
    this.chatClient =
        builder
            .defaultOptions(OpenAiChatOptions.builder().toolChoice("required").build())
            .defaultTools(new MemgptTools())
            .defaultSystem(SYSTEM_PROMPT)
            .build();
  }

  public AgentResponse prompt(String message) {
    UserMessage userMessage = new UserMessage(message);

    PromptTemplate pt = new PromptTemplate(systemPrompt);

    this.chatClient.prompt().user(userMessage.content).call().content();

    return null;
  }
}
