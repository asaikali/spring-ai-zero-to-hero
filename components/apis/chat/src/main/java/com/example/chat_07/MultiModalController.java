package com.example.chat_07;

import java.io.IOException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat/07")
public class MultiModalController {
  private ChatClient chatClient;

  @Value("classpath:/multimodal.test.png")
  private Resource image;

  public MultiModalController(ChatClient.Builder builder) {
    this.chatClient = builder.build();
  }

  // Provider apps can register a "visionChatClient" bean to override the default model.
  // For Ollama, OllamaVisionConfig provides a llava-backed client since qwen3:32b has no vision.
  @Autowired(required = false)
  @Qualifier("visionChatClient")
  public void setVisionChatClient(ChatClient visionChatClient) {
    this.chatClient = visionChatClient;
  }

  @GetMapping("/explain")
  public String explain() throws IOException {
    return chatClient
        .prompt()
        .user(
            u ->
                u.text("Explain what do you see in this picture?")
                    .media(MimeTypeUtils.IMAGE_PNG, image))
        .call()
        .content();
  }
}
