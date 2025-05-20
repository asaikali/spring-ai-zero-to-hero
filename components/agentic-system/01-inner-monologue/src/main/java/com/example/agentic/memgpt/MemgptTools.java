package com.example.agentic.memgpt;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class MemgptTools {

  @Tool(description = "update the core memory")
  public void updateCoreMemory(String label, String oldValue, String newValue) {}

  @Tool(description = "send a message to the user", returnDirect = true)
  public String sendMessage(
      @ToolParam(description = "The message you want the user to see") String message,
      @ToolParam(description = "your private inner thoughts") String innerThoughts) {

    System.out.println(innerThoughts);
    System.out.println(message);

    return innerThoughts + " - " + message;
  }
}
