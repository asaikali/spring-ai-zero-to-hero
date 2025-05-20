package com.example;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "agent")
@Component
public class TargetProperties {
  private String discoveryUrl = "http://localhost:8080/agents/targets";

  public String getDiscoveryUrl() {
    return discoveryUrl;
  }

  public void setDiscoveryUrl(String discoveryUrl) {
    this.discoveryUrl = discoveryUrl;
  }
}
