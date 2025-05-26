package com.example.command.agent;

import com.example.command.agent.dto.AgentJson;
import com.example.command.agent.dto.ChatRequest;
import com.example.command.agent.dto.ChatResponse;
import java.util.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.Builder;

@Component
public class AgentContext {

  private final AgentProperties agentProperties;
  private final List<String> messages = new ArrayList<>();
  private final Builder restClientBuilder;
  private String currentAgentId;

  public AgentContext(AgentProperties agentProperties, RestClient.Builder restClientBuilder) {
    this.agentProperties = agentProperties;
    this.restClientBuilder = restClientBuilder;
  }

  public List<String> getMessages() {
    return messages;
  }

  public String getCurrentAgentId() {
    return currentAgentId;
  }

  public void setCurrentAgentId(String currentAgentId) {
    this.currentAgentId = currentAgentId;
  }

  public RestClient getAgentRestClient() {
    String targetUrl =
        "http://"
            + this.agentProperties.getHost()
            + ":"
            + this.agentProperties.getPort()
            + "/agents/inner-monologue";
    return restClientBuilder.baseUrl(targetUrl).build();
  }

  public AgentJson createAgent(String agentId) {
    RestClient targetRestClient = this.getAgentRestClient();
    return targetRestClient.post().uri("/{id}", agentId).retrieve().body(AgentJson.class);
  }

  public AgentJson getAgent(String agentId) {
    RestClient client = this.getAgentRestClient();
    return client.get().uri("/{id}", agentId).retrieve().body(AgentJson.class);
  }

  public List<String> listAgents() {
    RestClient client = this.getAgentRestClient();
    String[] ids = client.get().uri("").retrieve().body(String[].class);
    return ids != null ? List.of(ids) : List.of();
  }

  public ChatResponse sendMessage(String agentId, String userText) {
    RestClient client = this.getAgentRestClient();
    return client
        .post()
        .uri("/{id}/messages", agentId)
        .body(new ChatRequest(userText))
        .retrieve()
        .body(ChatResponse.class);
  }
}
