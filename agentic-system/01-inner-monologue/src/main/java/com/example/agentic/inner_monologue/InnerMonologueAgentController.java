package com.example.agentic.inner_monologue;

import com.example.agentic.AgentSystemDescriptor;
import com.example.agentic.AgentTargetInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/agents/inner-monologue")
public class InnerMonologueAgentController implements AgentSystemDescriptor {

  private final ChatClient.Builder builder;
  private Map<String, Agent> agents = new HashMap<>();

  public InnerMonologueAgentController(ChatClient.Builder builder) {
    this.builder = builder;
  }

  @Override
  public AgentTargetInfo getAgentTargetInfo() {
    return new AgentTargetInfo(
        "inner-monologue",
        "Inner Monologue",
        "Thought before action using tool calls",
        "/agents/inner-monologue");
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

  @GetMapping()
  public List<String> getAgentIds() {
    return this.agents.keySet().stream().collect(Collectors.toList());
  }
}
