package com.example.agentic;

import java.util.Comparator;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agents")
public class AgentTargetDiscoveryController {

  private final List<AgentSystemDescriptor> systems;

  public AgentTargetDiscoveryController(List<AgentSystemDescriptor> systems) {
    this.systems = systems;
  }

  @GetMapping("/targets")
  public List<AgentTargetInfo> getTargets() {
    return systems.stream()
        .map(AgentSystemDescriptor::getAgentTargetInfo)
        .sorted(Comparator.comparing(AgentTargetInfo::name))
        .toList();
  }
}
