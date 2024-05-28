package com.example.chat_05;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat2/05")
class FunctionController2 {
  private final ChatClient chatClient;

  public FunctionController2(ChatModel chatModel) {
    this.chatClient = ChatClient.create(chatModel);
  }

  @GetMapping("/weather")
  public String getWeather(@RequestParam(value = "city", defaultValue = "Toronto") String city) {

    return chatClient
        .prompt()
        .functions("weatherFunction")
        .user(
            u ->
                u.text(
                        """
                What is the current weather in {city}?
                """)
                    .param("city", city))
        .call()
        .content();
  }

  @GetMapping("/pack")
  public String getClothingRecommendation(
      @RequestParam(value = "city", defaultValue = "Toronto") String city) {

    return chatClient
        .prompt()
        .functions("weatherFunction")
        .user(
            u ->
                u.text(
                        """
                I am traveling to {city} what kind of cloths should I pack?
                  """)
                    .param("city", city))
        .call()
        .content();
  }
}
