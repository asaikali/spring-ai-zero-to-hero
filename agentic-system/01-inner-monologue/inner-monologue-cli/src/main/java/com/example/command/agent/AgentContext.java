package com.example.command.agent;

import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class AgentContext {

  private final List<String> messages = new ArrayList<>();
  private String currentAgentId;

  public List<String> getMessages() {
    return messages;
  }

  public String getCurrentAgentId() {
    return currentAgentId;
  }

  public void setCurrentAgentId(String currentAgentId) {
    this.currentAgentId = currentAgentId;
  }
}
