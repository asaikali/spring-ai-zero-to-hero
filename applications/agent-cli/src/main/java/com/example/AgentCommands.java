package com.example;

import java.util.*;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

@Command(command = "agent", description = "Agentic Chat Commands")
public class AgentCommands {

  private final List<String> messages = new ArrayList<>();
  private final Set<String> agents = new HashSet<>();
  private String currentAgentId;

  @Command(command = "create", description = "Create a new agent")
  public void create(@Option(longNames = "id", required = true) String id) {
    agents.add(id);
    currentAgentId = id;
    messages.clear();
    messages.add("[SYSTEM] Created agent with ID: " + id);
    messages.add("[SYSTEM] Switched to agent: " + id);
    printChat();
  }

  @Command(command = "switch", description = "Switch to an existing agent")
  public void switchAgent(@Option(longNames = "id", required = true) String id) {
    if (!agents.contains(id)) {
      System.out.println("[ERROR] No agent with ID: " + id);
      return;
    }
    currentAgentId = id;
    messages.clear();
    messages.add("[SYSTEM] Switched to agent: " + id);
    printChat();
  }

  @Command(command = "send", description = "Send a message to the current agent")
  public void send(@Option(longNames = "text", required = true) String text) {
    if (currentAgentId == null) {
      System.out.println("[ERROR] No active agent. Use `agent create` or `agent switch` first.");
      return;
    }
    messages.add("[USER] " + text);
    messages.add("[AGENT] Agent received: \"" + text + "\"");
    printChat();
  }

  @Command(command = "log", description = "Show the current chat log")
  public void log() {
    if (currentAgentId == null) {
      System.out.println("[ERROR] No active agent. Use `agent create` or `agent switch` first.");
      return;
    }
    printChat();
  }

  @Command(command = "status", description = "Show the currently active agent")
  public void status() {
    if (currentAgentId == null) {
      System.out.println("No active agent.");
    } else {
      System.out.println("Current agent: " + currentAgentId);
    }
  }

  @Command(command = "list", description = "List all created agents")
  public void list() {
    if (agents.isEmpty()) {
      System.out.println("No agents created yet.");
      return;
    }
    for (String id : agents) {
      String marker = Objects.equals(id, currentAgentId) ? "*" : " ";
      System.out.println(marker + " " + id);
    }
  }

  private void printChat() {
    System.out.println("\n=== Agentic Chat [" + currentAgentId + "] ===");
    messages.forEach(System.out::println);
    System.out.println("===============================\n");
  }
}
