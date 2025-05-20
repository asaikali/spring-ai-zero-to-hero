package com.example;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class TargetCommands {

  private final TargetContext targetCtx;

  public TargetCommands(TargetContext targetCtx) {
    this.targetCtx = targetCtx;
  }

  @ShellMethod(value = "List all available targets", key = "target list")
  public void listTargets() {
    var systems = targetCtx.getTargets();
    if (systems.isEmpty()) {
      System.out.println("No targets available.");
      return;
    }
    for (var entry : systems.entrySet()) {
      System.out.println(entry.getKey() + " -> " + entry.getValue());
    }
  }

  @ShellMethod(value = "Set the current target", key = "target use")
  public void useTarget(
      @ShellOption(
              value = {"--name"},
              valueProvider = TargetNameValueProvider.class)
          String name) {
    var systems = targetCtx.getTargets();
    if (!systems.containsKey(name)) {
      System.out.println("[ERROR] No target with name: " + name);
      return;
    }
    targetCtx.setCurrentTargetName(name);
    System.out.println("Switched to target: " + name);
  }

  @ShellMethod(value = "Show the currently active target", key = "target status")
  public void targetStatus() {
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
