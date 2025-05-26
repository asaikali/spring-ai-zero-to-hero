package com.example.agentic.model_directed_loop;

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
@RequestMapping("/agents/model-directed-loop")
public class ModelDirectedLoopAgentController {

  private final ChatClient.Builder builder;
  private Map<String, Agent> agents = new HashMap<>();

  public ModelDirectedLoopAgentController(ChatClient.Builder builder) {
    this.builder = builder;
  }

  @PostMapping("/{id}")
  public AgentJson createAgent(@PathVariable(name = "id") String agentId) {
    Agent agent = new Agent(this.builder, agentId);
    agents.put(agentId, agent);
    return AgentJson.from(agent);
  }

  @GetMapping("/{id}")
  public AgentJson getAgent(@PathVariable(name = "id") String agentId) {
    Agent agent = this.agents.get(agentId);
    if (agent == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Agent not found");
    }
    return AgentJson.from(agent);
  }

  @GetMapping(path = {"", "/"})
  public List<String> getAgentIds() {
    return this.agents.keySet().stream().collect(Collectors.toList());
  }

  @PostMapping("/{id}/messages")
  public ChatTraceResponse sendMessage(
      @PathVariable(name = "id") String agentId, @RequestBody ChatRequest request) {
    Agent agent = this.agents.get(agentId);
    if (agent == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Agent not found");
    }
    return agent.userMessage(request);
  }
}
