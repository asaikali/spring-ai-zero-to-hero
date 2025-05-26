package com.example.command.agent;

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
}
