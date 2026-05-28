package com.example;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaVisionConfig {

  // llava is used for vision since qwen3:32b does not support image input
  @Bean("visionChatClient")
  ChatClient visionChatClient(OllamaApi ollamaApi) {
    var visionModel =
        OllamaChatModel.builder()
            .ollamaApi(ollamaApi)
            .defaultOptions(OllamaChatOptions.builder().model("llava").build())
            .build();
    return ChatClient.create(visionModel);
  }
}
