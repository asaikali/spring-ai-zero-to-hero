package com.example.chat_06;

import java.util.List;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat/06")
public class RoleController {
  private final ChatClient chatClient;
  private final String personaPrompt =
      """
        You are a helpful experts on plants, however you are not allowed
        to answers any question about vegetables you can only
        answer questions about fruits.
        """;

  public RoleController(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @GetMapping("/fruit")
  public String fruitQuestion() {
    SystemMessage systemMessage = new SystemMessage(personaPrompt);
    UserMessage userMessage = new UserMessage("What is the color of a banana?");
    Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

    System.out.println(prompt.getContents());
    ChatResponse response = chatClient.call(prompt);
    Generation generation = response.getResult();
    AssistantMessage assistantMessage = generation.getOutput();

    return assistantMessage.getContent();
  }

  @GetMapping("/veg")
  public String vegetableQuestion() {
    SystemMessage systemMessage = new SystemMessage(personaPrompt);
    UserMessage userMessage = new UserMessage("What is the color of a carrot?");
    Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

    System.out.println(prompt.getInstructions());
    System.out.println("---");
    System.out.println(prompt.getContents());

    ChatResponse response = chatClient.call(prompt);
    Generation generation = response.getResult();
    AssistantMessage assistantMessage = generation.getOutput();

    prompt = new Prompt(List.of(systemMessage, userMessage, assistantMessage));
    return assistantMessage.getContent();
  }
}
