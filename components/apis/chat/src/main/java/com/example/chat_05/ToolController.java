package com.example.chat_05;

import ch.qos.logback.core.pattern.util.RestrictedEscapeUtil;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat/05")
class ToolController {
  private final ChatClient chatClient;

  public ToolController(ChatClient.Builder builder) {
    this.chatClient = builder.build();
  }

  @GetMapping("/time")
  public String time(@RequestParam(value = "city", defaultValue = "Toronto") String city) {

    return chatClient
        .prompt()
        .tools(new TimeTools())
        .user(
            u ->
                u.text(
                        """
                What is the current time in {city}?
                """)
                    .param("city", city))
        .call()
        .content();
  }

  @GetMapping("/dayOfWeek")
  public String tomorrow(@RequestParam(value = "city", defaultValue = "Toronto") String city) {

    return chatClient
        .prompt()
        .tools(new TimeTools())
        .user(
            u ->
                u.text(
                        """
                What day of the week is tomorrow in {city}?
                """)
                    .param("city", city))
        .call()
        .content();
  }

  @GetMapping("/weather")
  public String getWeather(@RequestParam(value = "city", defaultValue = "Toronto") String city) {

    return chatClient
        .prompt()
        .toolNames("weatherFunction")
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
        .toolNames("weatherFunction")
        .user(
            u ->
                u.text(
                        """
                I am traveling to {city} what kind of clothes should I pack?
                """)
                    .param("city", city))
        .call()
        .content();
  }

  @GetMapping("/search")
  public Restaurant[] search(
      @RequestParam(
              value = "query",
              defaultValue = "find me an italian restaurant for lunch for 4 people today")
          String query) {

    CallResponseSpec callResponseSpec = this.chatClient
        .prompt()
        .tools( new RestaurantSearch())
        .user(query)
        .call();

    Restaurant[] result = callResponseSpec.entity(Restaurant[].class);

    return result;

  }
}
