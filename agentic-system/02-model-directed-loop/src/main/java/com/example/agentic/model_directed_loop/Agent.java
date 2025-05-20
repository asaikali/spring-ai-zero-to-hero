package com.example.agentic.model_directed_loop;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
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

        You may also set `request_reinvocation` to true if you want the agent to continue thinking after the tool call completes. This allows you to plan across multiple steps.

        Use `request_reinvocation: true` when the task is not yet complete. Use `false` or omit it to end the interaction.

        Never skip inner thoughts. Never output anything except by calling the `send_message` tool.
      """;

  private final String id;
  private final ChatClient chatClient;

  public Agent(ChatClient.Builder builder, String id) {
    this.id = id;

    var memory =
        MessageWindowChatMemory.builder()
            .chatMemoryRepository(new InMemoryChatMemoryRepository())
            .build();
    var chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(memory).build();

    this.chatClient =
        builder
            .clone()
            .defaultOptions(OpenAiChatOptions.builder().toolChoice("required").build())
            .defaultTools(new AgentTools())
            .defaultSystem(SYSTEM_PROMPT)
            .defaultAdvisors(chatMemoryAdvisor)
            .build();
  }

  public ChatResponse userMessage(ChatRequest request) {
    // Send the user message
    this.chatClient.prompt().user(request.text()).call();

    // Run the loop until the model says to stop
    ChatResponse latest = null;
    while (true) {
      String json = this.chatClient.prompt().call().content();
      latest = JsonParser.fromJson(json, ChatResponse.class);

      // Exit loop if no reinvocation requested
      if (latest.requestReinvocation() == false) {
        break;
      }
    }

    return latest;
  }

  public String getId() {
    return id;
  }

  public String getSystemPrompt() {
    return SYSTEM_PROMPT;
  }
}
