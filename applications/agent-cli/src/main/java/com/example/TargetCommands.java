package com.example;

import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

@Command(command = "target", description = "Target system commands")
class TargetCommands {

  private final TargetContext targetCtx;

  public TargetCommands(TargetContext targetCtx) {
    this.targetCtx = targetCtx;
  }

  @Command(command = "list", description = "List all available targets")
  public void list() {
    var systems = targetCtx.getTargets();
    if (systems.isEmpty()) {
      System.out.println("No targets available.");
      return;
    }
    for (var entry : systems.entrySet()) {
      System.out.println(entry.getKey() + " -> " + entry.getValue());
    }
  }

  @Command(command = "use", description = "Set the current target")
  public void use(@Option(longNames = "name", required = true) String name) {
    var systems = targetCtx.getTargets();
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
    var systems = targetCtx.getTargets();
    String url = systems.getOrDefault(current, "unknown URL");
    System.out.println("Current target: " + current);
    System.out.println("URL: " + url);
  }
}
