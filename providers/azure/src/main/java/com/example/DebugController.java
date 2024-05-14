package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugController {
  @Value("${spring.ai.openai.api-key:notfound}")
  private String apiKey;

  @GetMapping("/debug")
  public String getDebug() {
    return apiKey;
  }
}
