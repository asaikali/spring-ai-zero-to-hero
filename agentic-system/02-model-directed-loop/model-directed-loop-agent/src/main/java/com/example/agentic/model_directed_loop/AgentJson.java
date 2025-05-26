package com.example.agentic.model_directed_loop;

public record AgentJson(String id, String systemPrompt) {
  public static AgentJson from(Agent agent) {
    return new AgentJson(agent.getId(), agent.getSystemPrompt());
  }
}
