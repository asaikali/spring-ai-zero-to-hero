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

import com.logaritex.mcp.spring.SpringAiMcpAnnotationProvider;
import io.modelcontextprotocol.server.McpServerFeatures.SyncCompletionSpecification;
import io.modelcontextprotocol.server.McpServerFeatures.SyncPromptSpecification;
import io.modelcontextprotocol.server.McpServerFeatures.SyncResourceSpecification;
import java.util.List;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class McpServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(McpServerApplication.class, args);
  }

  @Bean
  public ToolCallbackProvider weatherTools(WeatherService weatherService) {
    return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
  }

  @Bean
  public List<SyncResourceSpecification> resourceSpecs(
      UserProfileResourceProvider userProfileResourceProvider) {
    return SpringAiMcpAnnotationProvider.createSyncResourceSpecifications(
        List.of(userProfileResourceProvider));
  }

  @Bean
  public List<SyncPromptSpecification> promptSpecs(PromptProvider promptProvider) {
    return SpringAiMcpAnnotationProvider.createSyncPromptSpecifications(List.of(promptProvider));
  }

  @Bean
  public List<SyncCompletionSpecification> completionSpecs(
      AutocompleteProvider autocompleteProvider) {
    return SpringAiMcpAnnotationProvider.createSyncCompleteSpecifications(
        List.of(autocompleteProvider));
  }
}
