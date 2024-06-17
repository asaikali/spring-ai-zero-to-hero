package com.example.mem_01;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mem/01")
public class StatelessController {

  private final ChatClient chatClient;

  @Autowired
  public StatelessController(ChatModel chatModel, ChatClient.Builder builder) {
    this.chatClient = builder.build();
  }

  @GetMapping("/hello")
  public String query(
      @RequestParam(
              value = "message",
              defaultValue = "Hello my name is John, what is the capital of France?")
          String message) {

    return this.chatClient.prompt().user(message).call().content();
  }

  @GetMapping("/name")
  public String name() {
    return this.chatClient.prompt().user("What is my name?").call().content();
  }
}
