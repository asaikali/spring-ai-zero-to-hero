package com.example;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TargetContext {

  private final RestClient restClient;
  private final TargetProperties props;
  private Map<String, String> targets = new HashMap<>();
  private String currentTargetName;

  public TargetContext(TargetProperties props, RestClient.Builder restClientBuilder) {
    this.props = props;
    this.restClient = restClientBuilder.baseUrl(props.getDiscoveryUrl()).build();
    refreshTargets(); // optional: load at startup
  }

  public void refreshTargets() {
    AgentTargetInfo[] result = restClient.get().uri("").retrieve().body(AgentTargetInfo[].class);

    if (result != null) {
      this.targets =
          Arrays.stream(result)
              .collect(Collectors.toMap(AgentTargetInfo::name, AgentTargetInfo::basePath));
    }
  }

  public Map<String, String> getTargets() {
    return targets;
  }

  public String getCurrentTargetName() {
    return currentTargetName;
  }

  public void setCurrentTargetName(String currentTargetName) {
    this.currentTargetName = currentTargetName;
  }
}
