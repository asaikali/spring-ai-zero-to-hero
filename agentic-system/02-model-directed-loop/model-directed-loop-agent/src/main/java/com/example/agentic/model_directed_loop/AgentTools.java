package com.example.agentic.model_directed_loop;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class AgentTools {

  @Tool(name = "send_message", description = "send a message to the user", returnDirect = true)
  public ChatResponse sendMessage(
      @ToolParam(description = "The message you want the user to see") String message,
      @ToolParam(description = "your private inner thoughts") String innerThoughts,
      @ToolParam(description = "Set to true to keep thinking after this message")
          Boolean requestReinvocation) {
    return new ChatResponse(message, innerThoughts, requestReinvocation);
  }
}
