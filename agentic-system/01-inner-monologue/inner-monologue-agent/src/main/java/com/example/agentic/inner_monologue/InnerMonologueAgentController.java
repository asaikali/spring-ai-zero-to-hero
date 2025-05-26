package com.example.agentic.inner_monologue;

import com.example.agentic.inner_monologue.dto.AgentJson;
import com.example.agentic.inner_monologue.dto.ChatRequest;
import com.example.agentic.inner_monologue.dto.ChatResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/agents/inner-monologue")
public class InnerMonologueAgentController {

  private final ChatClient.Builder builder;
  private Map<String, Agent> agents = new HashMap<>();

  public InnerMonologueAgentController(ChatClient.Builder builder) {
    this.builder = builder;
  }

  @PostMapping("/{id}")
  public AgentJson createAgent(@PathVariable(name = "id") String agentId) {
    Agent agent = new Agent(this.builder, agentId);
    agents.put(agentId, agent);
    return toAgentJson(agent);
  }

  @GetMapping("/{id}")
  public AgentJson getAgent(@PathVariable(name = "id") String agentId) {
    Agent agent = this.agents.get(agentId);
    if (agent == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Agent not found");
    }
    return toAgentJson(agent);
  }

  @GetMapping(path = {"", "/"})
  public List<String> getAgentIds() {
    return this.agents.keySet().stream().collect(Collectors.toList());
  }

  @PostMapping("/{id}/messages")
  public ChatResponse sendMessage(
      @PathVariable(name = "id") String agentId, @RequestBody ChatRequest request) {
    Agent agent = this.agents.get(agentId);
    if (agent == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Agent not found");
    }

    return agent.userMessage(request);
  }

  public AgentJson toAgentJson(Agent agent) {
    return new AgentJson(agent.getId(), agent.getSystemPrompt());
  }
}
