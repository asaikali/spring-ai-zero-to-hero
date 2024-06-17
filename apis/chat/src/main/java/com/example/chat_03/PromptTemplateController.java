package com.example.chat_03;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/chat/03")
public class PromptTemplateController {
  private final ChatClient chatClient;

  public PromptTemplateController(ChatClient.Builder builder) {
    this.chatClient = builder.build();
  }

  @GetMapping("/joke")
  public String getJoke(@RequestParam(value = "topic", defaultValue = "cows") String topic) {

    // Prompt template enables us to safely inject user input into the prompt
    // text in {} is replaced by the value of the variable with the same name.
    // PromptTemplate is a commonly used class with Spring AI
    return chatClient
        .prompt()
        .user(u -> u.text("Tell me a joke about {topic}").param("topic", topic))
        .call()
        .content();
  }

  @GetMapping("/plays")
  public String getPlays(
      @RequestParam(value = "author", defaultValue = "Shakespeare") String topic) {

    return chatClient
        .prompt()
        .user(u -> u.text(new ClassPathResource("prompts/plays.st")).param("author", topic))
        .call()
        .content();
  }
}
