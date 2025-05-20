package com.example.agentic.inner_monologue;

import java.util.HashMap;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mono/01")
public class InnerMonologueAgentController {

  private final ChatClient.Builder builder;
  private Map<String, Agent> agents = new HashMap<>();

  public InnerMonologueAgentController(ChatClient.Builder builder) {
    this.builder = builder;
  }

  @GetMapping("/create/{id}")
  public Agent createAgent(@PathVariable(name = "id") String agentId) {
    Agent agent = new Agent(this.builder, agentId);
    agents.put(agentId, agent);
    return agent;
  }
}
