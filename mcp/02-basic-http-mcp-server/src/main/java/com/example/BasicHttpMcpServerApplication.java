package com.example;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BasicHttpMcpServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(BasicHttpMcpServerApplication.class, args);
  }

  @Bean
  public ToolCallbackProvider weatherToolsProvider(WeatherTools weatherTools) {
    var toolCallbackProvider =
        MethodToolCallbackProvider.builder().toolObjects(weatherTools).build();
    return toolCallbackProvider;
  }
}
