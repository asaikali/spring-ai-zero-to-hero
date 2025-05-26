package com.example.command.agent;

import static com.example.JsonUtils.toPrettyJson;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

@Command(group = "Agent Commands")
public class AgentCommands {

  private static final List<String> MATRIX_AGENT_NAMES =
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

  private final AgentContext agentContext;
  private final Terminal terminal;

  public AgentCommands(AgentContext agentContext, Terminal terminal) {
    this.agentContext = agentContext;
    this.terminal = terminal;
  }

  @Command(command = "create", description = "Create a new agent")
  public void create(@Option(longNames = "id", required = false) String id) {
    if (id == null || id.isBlank()) {
      id = generateUniqueAgentId();
    }
    this.agentContext.createAgent(id);
    agentContext.setCurrentAgentId(id);
    agentContext.getMessages().clear();
    agentContext.getMessages().add("[SYSTEM] Created agent with ID: " + id);
    agentContext.getMessages().add("[SYSTEM] Switched to agent: " + id + "\n");
    printChat();
  }

  private String generateUniqueAgentId() {
    List<String> existing = agentContext.listAgents();

    for (int i = 0; i < 100; i++) {
      String candidate =
          MATRIX_AGENT_NAMES.get(ThreadLocalRandom.current().nextInt(MATRIX_AGENT_NAMES.size()));
      if (!existing.contains(candidate)) {
        return candidate;
      }
    }

    // Fallback to suffix
    int suffix = 1;
    while (true) {
      String candidate = "neo-" + suffix++;
      if (!existing.contains(candidate)) {
        return candidate;
      }
    }
  }

  @Command(command = "switch", description = "Switch to an existing agent")
  public void switchAgent(@Option(longNames = "id", required = true) String id) {
    List<String> ids = agentContext.listAgents();
    if (!ids.contains(id)) {
      System.out.println("[ERROR] No agent with ID: " + id + " on server.");
      return;
    }
    agentContext.setCurrentAgentId(id);
    agentContext.getMessages().clear();
    agentContext.getMessages().add("[SYSTEM] Switched to agent: " + id + "\n");
    printChat();
    refreshPrompt();
  }

  @Command(command = "send", description = "Send a message to the current agent")
  public void send(String text) {
    String agentId = agentContext.getCurrentAgentId();
    if (agentId == null) {
      System.out.println("[ERROR] No active agent. Use `agent create` or `agent switch` first.");
      return;
    }

    agentContext.getMessages().add("[USER] " + text);

    try {
      var response = agentContext.sendMessage(agentId, text);
      agentContext.getMessages().add("[THOUGHT] " + response.innerThoughts());
      agentContext.getMessages().add("[AGENT] " + response.message() + "\n");
    } catch (Exception e) {
      agentContext.getMessages().add("[ERROR] Failed to send message: " + e.getMessage());
    }

    printChat();
  }

  @Command(command = "log", description = "Show the current chat log")
  public void log() {
    if (agentContext.getCurrentAgentId() == null) {
      System.out.println("[ERROR] No active agent. Use `agent create` or `agent switch` first.");
      return;
    }
    printChat();
  }

  @Command(command = "status", description = "Show the currently active agent")
  public void status() {
    String agentId = agentContext.getCurrentAgentId();
    if (agentId == null) {
      System.out.println("No active agent.");
      return;
    }

    var agent = agentContext.getAgent(agentId);
    String json = toPrettyJson(agent);
    System.out.println(json);
  }

  @Command(command = "list", description = "List all created agents")
  public void list() {
    List<String> ids = agentContext.listAgents();

    if (ids.isEmpty()) {
      System.out.println("No agents found on the server.");
      return;
    }

    String current = agentContext.getCurrentAgentId();
    for (String id : ids) {
      String marker = Objects.equals(id, current) ? "*" : " ";
      System.out.println(marker + " " + id);
    }
  }

  private void printChat() {
    String agentId = agentContext.getCurrentAgentId();
    System.out.println("\n=== Agentic Chat [" + agentId + "] ===");
    agentContext.getMessages().forEach(System.out::println);
    System.out.println("===============================\n");
  }

  private void refreshPrompt() {
    this.terminal.writer().println(); // moves cursor to new line if needed
    terminal.flush(); // force update
  }
}
