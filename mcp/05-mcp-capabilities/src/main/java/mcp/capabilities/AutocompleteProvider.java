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

import io.modelcontextprotocol.server.McpServerFeatures.SyncCompletionSpecification;
import io.modelcontextprotocol.spec.McpSchema.CompleteResult;
import io.modelcontextprotocol.spec.McpSchema.CompleteResult.CompleteCompletion;
import io.modelcontextprotocol.spec.McpSchema.PromptReference;
import io.modelcontextprotocol.spec.McpSchema.ResourceReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Provides MCP completion specifications for autocomplete support.
 *
 * <p>In Spring AI 2.0 completions are registered by constructing {@link
 * SyncCompletionSpecification} objects directly — no @McpComplete annotations needed.
 *
 * <p>Two reference types are supported:
 *
 * <ul>
 *   <li>{@link ResourceReference} — for resource template URIs (e.g. {@code
 *       user-status://{username}})
 *   <li>{@link PromptReference} — for prompt argument values (e.g. the {@code name} arg in {@code
 *       personalized-message})
 * </ul>
 */
@Service
public class AutocompleteProvider {

  private final Map<String, List<String>> usernamesByPrefix = new HashMap<>();
  private final Map<String, List<String>> countriesByPrefix = new HashMap<>();

  public AutocompleteProvider() {
    usernamesByPrefix.put("a", List.of("alex123", "admin", "alice_wonder", "andrew99"));
    usernamesByPrefix.put("b", List.of("bob_builder", "blue_sky", "batman", "butterfly"));
    usernamesByPrefix.put("c", List.of("charlie", "cool_cat", "coder42", "captain_marvel"));
    usernamesByPrefix.put("d", List.of("david_dev", "dragon_slayer", "diamond_hand", "dancer"));
    usernamesByPrefix.put("j", List.of("john_doe", "java_expert", "jazz_lover", "jupiter"));
    usernamesByPrefix.put("m", List.of("martin123", "moon_walker", "master_chef", "music_fan"));
    usernamesByPrefix.put("s", List.of("sarah", "super_coder", "star_gazer", "swift_dev"));
    usernamesByPrefix.put("t", List.of("tech_guru", "traveler", "tiger", "tester101"));

    countriesByPrefix.put(
        "a", List.of("Afghanistan", "Albania", "Algeria", "Argentina", "Australia", "Austria"));
    countriesByPrefix.put("b", List.of("Bahamas", "Belgium", "Brazil", "Bulgaria"));
    countriesByPrefix.put("c", List.of("Canada", "Chile", "China", "Colombia", "Croatia"));
    countriesByPrefix.put("f", List.of("Finland", "France"));
    countriesByPrefix.put("g", List.of("Germany", "Greece"));
    countriesByPrefix.put("i", List.of("Iceland", "India", "Indonesia", "Ireland", "Italy"));
    countriesByPrefix.put("j", List.of("Japan"));
    countriesByPrefix.put("u", List.of("Uganda", "Ukraine", "United Kingdom", "United States"));
  }

  /** Returns all completion specifications to be registered with the MCP server. */
  public List<SyncCompletionSpecification> specifications() {
    return List.of(usernameForResourceSpec(), nameForPromptSpec(), countryForPromptSpec());
  }

  // --- username completion for user-status resource template ---

  private SyncCompletionSpecification usernameForResourceSpec() {
    return new SyncCompletionSpecification(
        new ResourceReference("user-status://{username}"),
        (exchange, request) -> {
          String prefix = request.argument().value().toLowerCase();
          List<String> matches = completionsFor(usernamesByPrefix, prefix);
          return new CompleteResult(new CompleteCompletion(matches, matches.size(), false));
        });
  }

  // --- name completion for personalized-message prompt ---

  private SyncCompletionSpecification nameForPromptSpec() {
    return new SyncCompletionSpecification(
        new PromptReference("personalized-message"),
        (exchange, request) -> {
          String prefix = request.argument().value().toLowerCase();
          List<String> matches = completionsFor(usernamesByPrefix, prefix);
          return new CompleteResult(new CompleteCompletion(matches, matches.size(), false));
        });
  }

  // --- country completion for travel-planner prompt ---

  private SyncCompletionSpecification countryForPromptSpec() {
    return new SyncCompletionSpecification(
        new PromptReference("travel-planner"),
        (exchange, request) -> {
          String prefix = request.argument().value().toLowerCase();
          List<String> matches = completionsFor(countriesByPrefix, prefix);
          return new CompleteResult(new CompleteCompletion(matches, matches.size(), false));
        });
  }

  // --- helpers ---

  private List<String> completionsFor(Map<String, List<String>> database, String prefix) {
    if (prefix.isEmpty()) {
      return List.of();
    }
    String firstLetter = prefix.substring(0, 1);
    return database.getOrDefault(firstLetter, List.of()).stream()
        .filter(s -> s.toLowerCase().startsWith(prefix))
        .toList();
  }
}
