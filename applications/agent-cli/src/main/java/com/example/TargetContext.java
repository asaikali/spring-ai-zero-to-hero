package com.example;

import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class TargetContext {

  private final Map<String, String> targets;
  private String currentTargetName;
  private final TargetProperties targetProps;

  public TargetContext(TargetProperties targetProps) {
    this.targetProps = targetProps;
    this.targets = this.targetProps.getResolvedSystems();
  }

  public String getCurrentTargetName() {
    return currentTargetName;
  }

  public Map<String, String> getTargets() {
    return targets;
  }

  public void setCurrentTargetName(String currentTargetName) {
    this.currentTargetName = currentTargetName;
  }
}
