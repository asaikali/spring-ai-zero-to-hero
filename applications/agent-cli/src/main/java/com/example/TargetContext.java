package com.example;

import org.springframework.stereotype.Component;

@Component
public class TargetContext {
  private String currentTargetName;

  public String getCurrentTargetName() {
    return currentTargetName;
  }

  public void setCurrentTargetName(String currentTargetName) {
    this.currentTargetName = currentTargetName;
  }
}
