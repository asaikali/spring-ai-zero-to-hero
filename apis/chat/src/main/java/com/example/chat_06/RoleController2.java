package com.example.chat_06;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat2/06")
public class RoleController2 {
  private final ChatClient chatClient;
  private final String personaPrompt =
      """
      You are a helpful experts on plants, however you are not allowed
      to answers any question about vegetables you can only
      answer questions about fruits.
      """;

  public RoleController2(ChatClient.Builder builder) {
    this.chatClient = builder.defaultSystem(personaPrompt).build();
  }

  @GetMapping("/fruit")
  public String fruitQuestion() {
    return chatClient.prompt().user("What is the color of a banana?").call().content();
  }

  @GetMapping("/veg")
  public String vegetableQuestion() {
    return chatClient.prompt().user("What is the color of a carrot?").call().content();
  }
}
