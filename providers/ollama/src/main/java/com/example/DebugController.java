package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugController {

  @GetMapping("/debug")
  public String getDebug() {
    return "You are running Ollama Application";
  }
}
