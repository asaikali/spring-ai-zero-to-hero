package com.example.command.agent;

import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class AgentContext {

  private final List<String> messages = new ArrayList<>();
  private final Set<String> agents = new HashSet<>();
  private String currentAgentId;

  public List<String> getMessages() {
    return messages;
  }

  public Set<String> getAgents() {
    return agents;
  }

  public String getCurrentAgentId() {
    return currentAgentId;
  }

  public void setCurrentAgentId(String currentAgentId) {
    this.currentAgentId = currentAgentId;
  }
}
