package com.example.chat_08;

import org.springframework.ai.chat.model.StreamingChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat/08")
public class StreamingChatModelController {
  private final StreamingChatModel chatModel;

  public StreamingChatModelController(StreamingChatModel chatModel) {
    this.chatModel = chatModel;
  }

  @GetMapping("/essay")
  public Flux<String> getJoke(
      @RequestParam(value = "topic", defaultValue = "Impact of AI on Society") String topic) {

    var promptTemplate = new PromptTemplate("Write an essay about {topic} ");
    promptTemplate.add("topic", topic);

    return chatModel.stream(promptTemplate.render());
  }
}
