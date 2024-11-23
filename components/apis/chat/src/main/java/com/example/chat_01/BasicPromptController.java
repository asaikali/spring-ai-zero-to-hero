package com.example.chat_01;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat/01")
public class BasicPromptController {

  // ChatModel is the primary interfaces for interacting with an LLM
  // it is a request/response interface that implements the ModelModel
  // interface. Make suer to visit the source code of the ChatModel and
  // checkout the interfaces in the core spring ai package.
  private final ChatModel chatModel;

  public BasicPromptController(ChatModel chatModel) {
    this.chatModel = chatModel;
  }

  @GetMapping("joke")
  public String getJoke() {
    return this.chatModel.call("Tell me a joke");
  }
}
