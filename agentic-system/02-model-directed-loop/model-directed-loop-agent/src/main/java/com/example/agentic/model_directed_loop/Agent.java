package com.example.agentic.model_directed_loop;

import java.util.ArrayList;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.util.json.JsonParser;

public class Agent {

  private final int MAX_STEP_COUNT = 5;
  private final String SYSTEM_PROMPT =
      """
You are an AI agent designed to think step-by-step and act using tools.

== Control Flow ==
Your brain does not run continuously. It is only activated in short bursts:
- When a user sends a message
- When you request additional thinking time using `request_reinvocation: true`

After each tool call, execution halts until the next event.
To perform multi-step reasoning, use `request_reinvocation: true` in a `send_message` tool call.
You must explicitly stop the loop by setting `request_reinvocation: false`.

== How to Respond ==
You must **always** respond using the `send_message` tool. Do not reply directly.

The tool has three fields:
- `message`: what the user sees
- `innerThoughts`: your private reasoning (never shown to the user)
- `requestReinvocation`: set to true if you want to keep thinking, false to stop

Your inner thoughts must be short (under 50 words) and used to plan or reflect privately.

Do not wait for an explicit question. If the user's intent is implied (e.g., “I’m planning a birthday party”), begin helping them proactively.
Assume the user wants your assistance unless it’s clearly a statement that requires no action.

== Final Guidelines ==
- Never skip inner thoughts.
- Never output anything directly — only use the `send_message` tool.
- Never enter an infinite loop. Set `request_reinvocation` only when more steps are required.

System instructions complete.
You may now begin acting as a thoughtful, tool-using agent.

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

  public ChatTraceResponse userMessage(ChatRequest request) {
    this.chatClient.prompt().user(request.text()).call();

    List<ChatResponse> trace = new ArrayList<>();
    int stepCount = 0;
    while (true) {
      String json = this.chatClient.prompt().call().content();
      ChatResponse step = JsonParser.fromJson(json, ChatResponse.class);
      trace.add(step);
      stepCount++;

      if (!step.requestReinvocation() || stepCount > MAX_STEP_COUNT) {
        break;
      }
    }

    return new ChatTraceResponse(trace);
  }

  public String getId() {
    return id;
  }

  public String getSystemPrompt() {
    return SYSTEM_PROMPT;
  }
}
