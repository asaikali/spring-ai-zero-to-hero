package com.example.agentic.inner_monologue;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.util.json.JsonParser;

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

  private final String id;
  private final ChatClient chatClient;

  public Agent(ChatClient.Builder builder, String id) {
    this.id = id;
    this.chatClient =
        builder
            .clone()
            .defaultOptions(OpenAiChatOptions.builder().toolChoice("required").build())
            .defaultTools(new AgentTools())
            .defaultSystem(SYSTEM_PROMPT)
            .build();
  }

  public ChatResponse userMessage(ChatRequest request) {
    String json =
        this.chatClient.prompt().user(request.text()).call().content();
    ChatResponse result = JsonParser.fromJson(json,ChatResponse.class);
    return result;
  }



  public String getId() {
    return id;
  }

  public String getSystemPrompt() {
    return SYSTEM_PROMPT;
  }
}
