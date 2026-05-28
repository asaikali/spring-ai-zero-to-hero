package com.example;

import io.micrometer.observation.ObservationPredicate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.ollama.api.ThinkOption;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.observation.ServerRequestObservationContext;

@SpringBootApplication
public class OllamaApplication {
  public static void main(String[] args) {
    SpringApplication.run(OllamaApplication.class, args);
  }

  // Disable qwen3 thinking mode - it adds latency without benefit for these demos
  @Bean
  ApplicationRunner disableThinkingMode(OllamaChatModel model) {
    return args -> {
      var options = (OllamaChatOptions) model.getDefaultOptions();
      options.setThinkOption(ThinkOption.ThinkBoolean.DISABLED);
    };
  }

  // Optional suppress the actuator server observations. This hides the actuator
  // prometheus traces.
  @Bean
  ObservationPredicate noActuatorServerObservations() {
    return (name, context) -> {
      if (name.equals("http.server.requests")
          && context instanceof ServerRequestObservationContext serverContext) {
        return !serverContext.getCarrier().getRequestURI().startsWith("/actuator");
      } else {
        return true;
      }
    };
  }
}
