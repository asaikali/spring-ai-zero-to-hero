package com.example.agentic.inner_monologue;

import com.example.agentic.AgentSystemDescriptor;
import com.example.agentic.AgentTargetInfo;
import java.util.HashMap;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  @GetMapping("/create/{id}")
  public Agent createAgent(@PathVariable(name = "id") String agentId) {
    Agent agent = new Agent(this.builder, agentId);
    agents.put(agentId, agent);
    return agent;
  }
}
