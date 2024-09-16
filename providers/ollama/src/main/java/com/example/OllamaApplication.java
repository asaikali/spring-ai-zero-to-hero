package com.example;

import io.micrometer.observation.ObservationPredicate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.observation.ServerRequestObservationContext;

@SpringBootApplication
public class OllamaApplication {
  public static void main(String[] args) {
    SpringApplication.run(OllamaApplication.class, args);
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
