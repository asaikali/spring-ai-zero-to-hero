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

import io.modelcontextprotocol.server.McpServerFeatures.SyncResourceSpecification;
import io.modelcontextprotocol.spec.McpSchema.ReadResourceResult;
import io.modelcontextprotocol.spec.McpSchema.Resource;
import io.modelcontextprotocol.spec.McpSchema.ResourceContents;
import io.modelcontextprotocol.spec.McpSchema.TextResourceContents;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Provides MCP resource specifications for user profile data.
 *
 * <p>In Spring AI 2.0 resource specifications are constructed directly via {@link
 * SyncResourceSpecification} instead of using the third-party @McpResource annotation. Each
 * resource maps a URI (or URI template) to a handler lambda.
 */
@Service
public class UserProfileResourceProvider {

  private final Map<String, Map<String, String>> userProfiles = new HashMap<>();

  public UserProfileResourceProvider() {
    userProfiles.put(
        "john",
        Map.of(
            "name", "John Smith",
            "email", "john.smith@example.com",
            "age", "32",
            "location", "New York",
            "status", "🟢 Online"));
    userProfiles.put(
        "jane",
        Map.of(
            "name", "Jane Doe",
            "email", "jane.doe@example.com",
            "age", "28",
            "location", "London",
            "status", "🟠 Away"));
    userProfiles.put(
        "bob",
        Map.of(
            "name", "Bob Johnson",
            "email", "bob.johnson@example.com",
            "age", "45",
            "location", "Tokyo",
            "status", "⚪ Offline"));
    userProfiles.put(
        "alice",
        Map.of(
            "name", "Alice Brown",
            "email", "alice.brown@example.com",
            "age", "36",
            "location", "Sydney",
            "status", "🔴 Busy"));
  }

  /**
   * Returns all resource specifications to be registered with the MCP server.
   *
   * <p>Resources use URI templates like {@code user-profile://{username}} — the server registers
   * the template, and the client reads with a concrete URI such as {@code user-profile://alice}.
   * The handler extracts the variable by stripping the scheme prefix.
   */
  public List<SyncResourceSpecification> specifications() {
    return List.of(userProfileSpec(), userStatusSpec(), userAttributeSpec());
  }

  // --- user-profile://{username} ---

  private SyncResourceSpecification userProfileSpec() {
    return new SyncResourceSpecification(
        new Resource(
            "user-profile://{username}",
            "User Profile",
            "Full profile for a user (name, email, age, location)",
            "text/plain",
            null),
        (exchange, request) -> {
          String username = request.uri().replace("user-profile://", "");
          String text = formatProfile(userProfiles.getOrDefault(username.toLowerCase(), Map.of()));
          return new ReadResourceResult(
              List.of(new TextResourceContents(request.uri(), "text/plain", text)));
        });
  }

  // --- user-status://{username} ---

  private SyncResourceSpecification userStatusSpec() {
    return new SyncResourceSpecification(
        new Resource(
            "user-status://{username}",
            "User Status",
            "Online/offline status for a user",
            "text/plain",
            null),
        (exchange, request) -> {
          String username = request.uri().replace("user-status://", "");
          Map<String, String> profile = userProfiles.getOrDefault(username.toLowerCase(), Map.of());
          String status = profile.getOrDefault("status", "⚪ Offline");
          return new ReadResourceResult(
              List.of(new TextResourceContents(request.uri(), "text/plain", status)));
        });
  }

  // --- user-attribute://{username}/{attribute} ---

  private SyncResourceSpecification userAttributeSpec() {
    return new SyncResourceSpecification(
        new Resource(
            "user-attribute://{username}/{attribute}",
            "User Attribute",
            "A single attribute from a user profile (e.g. user-attribute://alice/location)",
            "text/plain",
            null),
        (exchange, request) -> {
          // URI: user-attribute://alice/location  → parts = ["alice", "location"]
          String path = request.uri().replace("user-attribute://", "");
          String[] parts = path.split("/", 2);
          String username = parts.length > 0 ? parts[0] : "";
          String attribute = parts.length > 1 ? parts[1] : "";
          Map<String, String> profile = userProfiles.getOrDefault(username.toLowerCase(), Map.of());
          String value = profile.getOrDefault(attribute, "attribute not found");
          List<ResourceContents> contents =
              List.of(
                  new TextResourceContents(
                      request.uri(), "text/plain", username + "'s " + attribute + ": " + value));
          return new ReadResourceResult(contents);
        });
  }

  // --- helpers ---

  private String formatProfile(Map<String, String> profile) {
    if (profile.isEmpty()) {
      return "User profile not found";
    }
    StringBuilder sb = new StringBuilder();
    profile.forEach((k, v) -> sb.append(k).append(": ").append(v).append("\n"));
    return sb.toString().trim();
  }
}
