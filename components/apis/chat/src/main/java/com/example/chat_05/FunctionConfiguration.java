package com.example.chat_05;

import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FunctionConfiguration {

  /*
   * We need to wrap the WeatherService in a java.util.function.Function so
   * that Spring AI can generate the JSON schema from the function input
   * and output objects. Spring AI configures the chat model with information
   * about this function.
   *
   * Not all LLMs support function calling, so in order to register this
   * function with the correct ChatModel it would be done using a model
   * specific ChatOptions which is not available in the classpath of this
   * project. A function can be registered globally and this what we do
   * in the specific projects.
   *
   * see providers/openai/src/main/resources/application.yaml for how the
   * function is registered with OpenAiChat model.
   *
   *
   */
  //  @Bean
  //  @Description("Get the weather in location")
  //  public Function<WeatherRequest, WeatherResponse> weatherFunction(WeatherService
  // weatherService) {
  //    return request -> weatherService.getCurrentWeather(request.city());
  //  }

  @Bean
  public FunctionCallback weatherFunctionCallback(WeatherService weatherService) {
    return FunctionCallback.builder()
        .description("Get the weather in location")
        .function(
            "weatherFunction",
            (WeatherRequest request) -> weatherService.getCurrentWeather(request.city()))
        .inputType(WeatherRequest.class)
        .build();
  }
}
