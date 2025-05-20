package com.example;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "target")
public class TargetProperties {

  private String host;
  private int port;
  private Map<String, String> systems = new HashMap<>();

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public Map<String, String> getResolvedSystems() {
    return systems.entrySet().stream()
        .collect(
            Collectors.toMap(Map.Entry::getKey, e -> "http://" + host + ":" + port + e.getValue()));
  }

  public void setSystems(Map<String, String> systems) {
    this.systems = systems;
  }
}
