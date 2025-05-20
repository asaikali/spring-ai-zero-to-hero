package com.example;

import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

@Command(command = "target", description = "Target system commands")
class TargetCommands {

  private final TargetContext targetCtx;
  private final TargetProperties targetProps;

  public TargetCommands(TargetContext targetCtx, TargetProperties targetProps) {
    this.targetCtx = targetCtx;
    this.targetProps = targetProps;
  }

  @Command(command = "list", description = "List all available targets")
  public void list() {
    var systems = targetProps.getResolvedSystems();
    if (systems.isEmpty()) {
      System.out.println("No targets available.");
      return;
    }
    for (var entry : systems.entrySet()) {
      System.out.println(entry.getKey());
    }
  }

  @Command(command = "use", description = "Set the current target")
  public void use(@Option(longNames = "name", required = true) String name) {
    var systems = targetProps.getResolvedSystems();
    if (!systems.containsKey(name)) {
      System.out.println("[ERROR] No target with name: " + name);
      return;
    }
    targetCtx.setCurrentTargetName(name);
    System.out.println("Switched to target: " + name);
  }

  @Command(command = "status", description = "Show the currently active target")
  public void status() {
    String current = targetCtx.getCurrentTargetName();
    if (current == null) {
      System.out.println("No active target.");
      return;
    }
    var systems = targetProps.getResolvedSystems();
    String url = systems.getOrDefault(current, "unknown URL");
    System.out.println("Current target: " + current);
    System.out.println("URL: " + url);
  }
}
