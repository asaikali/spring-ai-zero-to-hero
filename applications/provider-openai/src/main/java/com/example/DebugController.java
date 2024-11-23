package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugController {
  @Value("${spring.ai.openai.api-key:notfound}")
  private String apiKey;

  @Value("${spring.ai.openai.endpoint:notfound}")
  private String endpoint;

  @GetMapping("/debug")
  public String getDebug() {
    return "You are running OpenAI Application with apiKey=" + apiKey;
  }
}
