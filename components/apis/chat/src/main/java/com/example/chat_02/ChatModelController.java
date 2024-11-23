package com.example.chat_02;

import java.util.ArrayList;
import java.util.List;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat/02/model")
public class ChatModelController {
  private final ChatModel chatModel;

  public ChatModelController(ChatModel chatModel) {
    this.chatModel = chatModel;
  }

  @GetMapping("/joke")
  public String getJoke(@RequestParam(value = "topic", defaultValue = "cows") String topic) {

    // Prompt is the primary class that represents a request to an LLM.
    // it can be configured with more options to enable more complex interactions
    // with the AI service. We will see more options later.
    Prompt prompt = new Prompt("Tell me a joke about " + topic);

    // chat model takes a prompt and returns a chat response
    ChatResponse response = chatModel.call(prompt);

    // The response object contains a result object called a generation
    // containing the text from the AI LLM
    Generation generation = response.getResult();

    // The actual data from an LLM is stored in a Message object, there
    // are different types of messages. AssistantMessage indicates that the
    // contents came from the AI service.
    AssistantMessage assistantMessage = generation.getOutput();

    // All these layers of objects might seem to be overkills. However,
    // keep in mind that the same interfaces are used for dealing with
    // text, audio, video, images, and raw numbers. As such the underlying
    // low level interfaces need to be factored out in way, that enables
    // higher level interfaces to be built. The API you are see in this
    // controller is more like JDBC API as apposed to a higher level Spring
    // data jpa. Spring AI will be adding higher level interfaces on top
    // the low level interfaces you have seen so far.
    return assistantMessage.getContent();
  }

  @GetMapping("/threeJokes")
  public List<String> getThreeJokes() {

    // Prompt is the primary class that represents a request to an LLM.
    // it can be configured with more options to enable more complex interactions
    // with the AI service. We will see more options later.
    Prompt prompt = new Prompt("Tell me a joke");

    // chat model takes a prompt and returns a chat response
    ChatResponse response = chatModel.call(prompt);

    // The response object contains a result object called a generation
    // containing the text from the AI LLM
    List<Generation> generations = response.getResults();
    List<String> jokes = new ArrayList<>();
    for (var generation : generations) {
      AssistantMessage assistantMessage = generation.getOutput();
      jokes.add(assistantMessage.getContent());
    }

    return jokes;
  }
}
