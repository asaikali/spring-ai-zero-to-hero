package com.example;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

@Command(command = "agent", description = "Agentic Chat Commands")
public class AgentCommands {

  private final List<String> defaultAgentNames =
      List.of(
          "neo",
          "trinity",
          "morpheus",
          "smith",
          "cypher",
          "oracle",
          "apoc",
          "switch",
          "tank",
          "dozer");

  private final AgentContext ctx;

  public AgentCommands(AgentContext ctx) {
    this.ctx = ctx;
  }

  @Command(command = "create", description = "Create a new agent")
  public void create(@Option(longNames = "id", required = false) String id) {
    if (id == null || id.isBlank()) {
      id = generateUniqueAgentId();
    }
    ctx.getAgents().add(id);
    ctx.setCurrentAgentId(id);
    ctx.getMessages().clear();
    ctx.getMessages().add("[SYSTEM] Created agent with ID: " + id);
    ctx.getMessages().add("[SYSTEM] Switched to agent: " + id);
    printChat();
  }

  private String generateUniqueAgentId() {
    for (int i = 0; i < 100; i++) {
      String candidate =
          defaultAgentNames.get(ThreadLocalRandom.current().nextInt(defaultAgentNames.size()));
      if (!ctx.getAgents().contains(candidate)) {
        return candidate;
      }
    }
    // Fallback: neo-1, neo-2, etc.
    int suffix = 1;
    while (true) {
      String candidate = "neo-" + suffix++;
      if (!ctx.getAgents().contains(candidate)) {
        return candidate;
      }
    }
  }

  @Command(command = "switch", description = "Switch to an existing agent")
  public void switchAgent(@Option(longNames = "id", required = true) String id) {
    if (!ctx.getAgents().contains(id)) {
      System.out.println("[ERROR] No agent with ID: " + id);
      return;
    }
    ctx.setCurrentAgentId(id);
    ctx.getMessages().clear();
    ctx.getMessages().add("[SYSTEM] Switched to agent: " + id);
    printChat();
  }

  @Command(command = "send", description = "Send a message to the current agent")
  public void send(@Option(longNames = "text", required = true) String text) {
    if (ctx.getCurrentAgentId() == null) {
      System.out.println("[ERROR] No active agent. Use `agent create` or `agent switch` first.");
      return;
    }
    ctx.getMessages().add("[USER] " + text);
    ctx.getMessages().add("[AGENT] Agent received: \"" + text + "\"");
    printChat();
  }

  @Command(command = "log", description = "Show the current chat log")
  public void log() {
    if (ctx.getCurrentAgentId() == null) {
      System.out.println("[ERROR] No active agent. Use `agent create` or `agent switch` first.");
      return;
    }
    printChat();
  }

  @Command(command = "status", description = "Show the currently active agent")
  public void status() {
    if (ctx.getCurrentAgentId() == null) {
      System.out.println("No active agent.");
    } else {
      System.out.println("Current agent: " + ctx.getCurrentAgentId());
    }
  }

  @Command(command = "list", description = "List all created agents")
  public void list() {
    if (ctx.getAgents().isEmpty()) {
      System.out.println("No agents created yet.");
      return;
    }
    for (String id : ctx.getAgents()) {
      String marker = Objects.equals(id, ctx.getCurrentAgentId()) ? "*" : " ";
      System.out.println(marker + " " + id);
    }
  }

  private void printChat() {
    System.out.println("\n=== Agentic Chat [" + ctx.getCurrentAgentId() + "] ===");
    ctx.getMessages().forEach(System.out::println);
    System.out.println("===============================\n");
  }
}
