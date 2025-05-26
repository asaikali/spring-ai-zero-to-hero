package com.example;

import com.example.command.agent.AgentContext;
import com.example.command.target.TargetContext;
import com.example.dto.AgentJson;
import com.example.dto.ChatRequest;
import com.example.dto.ChatResponse;
import java.util.List;
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

  public AgentJson getAgent(String agentId) {
    RestClient client = targetContext.getCurrentTargetRestClient();
    return client.get().uri("/{id}", agentId).retrieve().body(AgentJson.class);
  }

  public List<String> listAgents() {
    RestClient client = targetContext.getCurrentTargetRestClient();
    String[] ids = client.get().uri("").retrieve().body(String[].class);
    return ids != null ? List.of(ids) : List.of();
  }

  public ChatResponse sendMessage(String agentId, String userText) {
    RestClient client = targetContext.getCurrentTargetRestClient();
    return client
        .post()
        .uri("/{id}/messages", agentId)
        .body(new ChatRequest(userText))
        .retrieve()
        .body(ChatResponse.class);
  }
}
