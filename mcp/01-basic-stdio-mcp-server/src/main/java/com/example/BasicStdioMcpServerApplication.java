package com.example;

import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BasicStdioMcpServerApplication {

  private static final Logger logger =
      LoggerFactory.getLogger(BasicStdioMcpServerApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(BasicStdioMcpServerApplication.class, args);
  }

  @Bean
  public ToolCallbackProvider weatherToolsProvider(WeatherTools weatherTools) {
    var toolCallbackProvider =
        MethodToolCallbackProvider.builder().toolObjects(weatherTools).build();
    logger.info(
        "Tools: "
            + Stream.of(toolCallbackProvider.getToolCallbacks())
                .map(tc -> tc.getToolDefinition())
                .toList());
    return toolCallbackProvider;
  }
}
