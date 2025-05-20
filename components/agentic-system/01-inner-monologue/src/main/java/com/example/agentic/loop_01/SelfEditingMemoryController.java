package com.example.agentic.loop_01;

import com.example.agentic.memgpt.MemgptTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loop/01")
public class SelfEditingMemoryController {

  private final ChatClient chatClient;

  @Autowired
  public SelfEditingMemoryController(ChatClient.Builder builder) {
    this.chatClient =
        builder
            .defaultTools(new MemgptTools())
            .defaultSystem(
                """
            Your an AI Agent that helps humans solve problems. While solving
            problems you have the ability think private thoughts these thoughts
            are yours only they will not be show to the user. Anything you want
            to send to the user you can do so using the sendMessage tools.


            Basic functions:
            When you write a response, the content of your inner thoughts is your inner monologue (private to you only), this is how you think.
            You should use your inner monologue to plan actions or think privately.
            Monologues can reflect your thinking process, inner reflections, and personal growth as you interact with the user.
            Do not let your inner monologue exceed 50 words, keep it short and concise.
            To send a visible message to the user, use the SendMessage function.
            'SendMessage' is the ONLY action that sends a notification to the user. The user does not see anything else you do.
            Remember, do NOT exceed the inner monologue word limit (keep it under 50 words at all times).
            """)
            .build();
  }

  @GetMapping("/hello")
  public String query(@RequestParam(value = "message", defaultValue = "Hello") String message) {

    CallResponseSpec responseSpec = this.chatClient.prompt().user(message).call();

    String result = responseSpec.content();

    return result;
  }

  @GetMapping("/name")
  public String name() {
    return this.chatClient.prompt().user("What is my name?").call().content();
  }
}
