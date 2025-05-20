package com.example.agentic.inner_monologue;

public record AgentJson(String id, String systemPrompt) {
  public static AgentJson from(Agent agent) {
    return new AgentJson(agent.getId(), agent.getSystemPrompt());
  }
}
