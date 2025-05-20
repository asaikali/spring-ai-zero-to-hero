package com.example.agentic.inner_monologue;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class AgentTools {

  @Tool(name = "send_message", description = "send a message to the user", returnDirect = true)
  public ChatResponse sendMessage(
      @ToolParam(description = "The message you want the user to see") String message,
      @ToolParam(description = "your private inner thoughts") String innerThoughts) {
    return new ChatResponse(message, innerThoughts);
  }
}
