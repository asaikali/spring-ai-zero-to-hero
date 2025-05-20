package com.example;

import com.example.command.agent.AgentContext;
import com.example.command.target.TargetContext;
import com.example.dto.AgentJson;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AgentServiceClient {

  private final RestClient.Builder restClientBuilder;
  private final TargetContext targetContext;
  private final AgentContext agentContext;

  public AgentServiceClient(
      RestClient.Builder restClientBuilder,
      TargetContext targetContext,
      AgentContext agentContext) {
    this.restClientBuilder = restClientBuilder;
    this.targetContext = targetContext;
    this.agentContext = agentContext;
  }

  public AgentJson createAgent(String agentId) {
    RestClient targetRestClient = this.targetContext.getCurrentTargetRestClient();
    return targetRestClient.post().uri("/{id}", agentId).retrieve().body(AgentJson.class);
  }
}
