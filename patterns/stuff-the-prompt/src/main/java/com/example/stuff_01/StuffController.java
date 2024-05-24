package com.example.stuff_01;

import java.util.HashMap;
import java.util.Map;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stuffit/01")
public class StuffController {

  private final ChatClient chatClient;

  @Value("classpath:/docs/wikipedia-curling.md")
  private Resource docsToStuffResource;

  @Value("classpath:/prompts/qa-prompt.st")
  private Resource qaPromptResource;

  @Autowired
  public StuffController(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @GetMapping("/query")
  public String query(
      @RequestParam(
              value = "message",
              defaultValue =
                  "Which athletes won the mixed doubles gold medal in curling at the 2022 Winter Olympics?'")
          String message) {

    PromptTemplate promptTemplate = new PromptTemplate(qaPromptResource);
    Map<String, Object> map = new HashMap<>();
    map.put("question", message);
    map.put("context", docsToStuffResource);

    Prompt prompt = promptTemplate.create(map);
    return chatClient.call(prompt).getResult().getOutput().getContent();
  }
}
