/*
 * Copyright 2025 - 2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mcp.capabilities;

import io.modelcontextprotocol.server.McpServerFeatures.SyncPromptSpecification;
import io.modelcontextprotocol.spec.McpSchema.GetPromptResult;
import io.modelcontextprotocol.spec.McpSchema.LoggingLevel;
import io.modelcontextprotocol.spec.McpSchema.LoggingMessageNotification;
import io.modelcontextprotocol.spec.McpSchema.Prompt;
import io.modelcontextprotocol.spec.McpSchema.PromptArgument;
import io.modelcontextprotocol.spec.McpSchema.PromptMessage;
import io.modelcontextprotocol.spec.McpSchema.Role;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Provides MCP prompt specifications.
 *
 * <p>In Spring AI 2.0 prompts are registered by constructing {@link SyncPromptSpecification}
 * objects directly — no @McpPrompt / @McpArg annotations needed.
 */
@Service
public class PromptProvider {

  /** Returns all prompt specifications to be registered with the MCP server. */
  public List<SyncPromptSpecification> specifications() {
    return List.of(greetingSpec(), personalizedMessageSpec(), conversationStarterSpec());
  }

  // --- greeting ---

  private SyncPromptSpecification greetingSpec() {
    return new SyncPromptSpecification(
        new Prompt(
            "greeting",
            "A simple greeting prompt",
            List.of(new PromptArgument("name", "The name to greet", true))),
        (exchange, request) -> {
          String name =
              request.arguments() != null
                  ? (String) request.arguments().getOrDefault("name", "World")
                  : "World";
          return new GetPromptResult(
              "Greeting",
              List.of(
                  new PromptMessage(
                      Role.ASSISTANT,
                      new TextContent("Hello, " + name + "! Welcome to the MCP system."))));
        });
  }

  // --- personalized-message ---

  private SyncPromptSpecification personalizedMessageSpec() {
    return new SyncPromptSpecification(
        new Prompt(
            "personalized-message",
            "Generates a personalized message based on user information",
            List.of(
                new PromptArgument("name", "The user's name", true),
                new PromptArgument("age", "The user's age", false),
                new PromptArgument("interests", "The user's interests", false))),
        (exchange, request) -> {
          java.util.Map<String, Object> args =
              request.arguments() != null ? request.arguments() : java.util.Map.of();

          exchange.loggingNotification(
              LoggingMessageNotification.builder()
                  .level(LoggingLevel.INFO)
                  .data("personalized-message event")
                  .build());

          String name = (String) args.getOrDefault("name", "Friend");
          String age = (String) args.get("age");
          String interests = (String) args.get("interests");

          StringBuilder message = new StringBuilder("Hello, ").append(name).append("!\n\n");
          if (age != null) {
            int ageInt = Integer.parseInt(age);
            message.append("At ").append(ageInt).append(" years old, you have ");
            if (ageInt < 30) message.append("so much ahead of you.\n\n");
            else if (ageInt < 60) message.append("gained valuable life experience.\n\n");
            else message.append("accumulated wisdom to share with others.\n\n");
          }
          if (interests != null && !interests.isEmpty()) {
            message
                .append("Your interest in ")
                .append(interests)
                .append(" shows your curiosity and passion for learning.\n\n");
          }
          message.append(
              "I'm here to assist you with any questions you might have about the Model"
                  + " Context Protocol.");

          return new GetPromptResult(
              "Personalized Message",
              List.of(new PromptMessage(Role.ASSISTANT, new TextContent(message.toString()))));
        });
  }

  // --- conversation-starter ---

  private SyncPromptSpecification conversationStarterSpec() {
    return new SyncPromptSpecification(
        new Prompt("conversation-starter", "Provides a conversation starter with the system", null),
        (exchange, request) ->
            new GetPromptResult(
                "Conversation Starter",
                List.of(
                    new PromptMessage(
                        Role.ASSISTANT,
                        new TextContent("Hello! I'm the MCP assistant. How can I help you today?")),
                    new PromptMessage(
                        Role.USER,
                        new TextContent(
                            "I'd like to learn more about the Model Context Protocol.")),
                    new PromptMessage(
                        Role.ASSISTANT,
                        new TextContent(
                            "Great choice! MCP is a standardized way for servers to communicate"
                                + " with language models. What would you like to explore first?")))));
  }
}
