package com.example.agent.cot;

import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cot/bio/")
public class ChainOfThoughtController {

  private final ChatClient chatClient;

  @Value("classpath:/info/profile.pdf")
  private Resource profile;

  private ChainOfThoughtBioWriterAgent bioWriterAgent;

  @Autowired
  public ChainOfThoughtController(
      ChatModel chatModel,
      ChatClient.Builder builder,
      ChainOfThoughtBioWriterAgent bioWriterAgent) {
    this.chatClient = builder.build();
    this.bioWriterAgent = bioWriterAgent;
  }

  @GetMapping("/oneshot")
  public String oneShot(
      @RequestParam(
              value = "message",
              defaultValue = "Hello my name is John, what is the capital of France?")
          String message) {

    LinkedProfile profile = new LinkedProfile(this.profile);
    String bio =
        this.chatClient
            .prompt()
            .user(
                userSpec ->
                    userSpec
                        .text(
                            """
      Write a one paragraph professional biography suitable for conference presentation based on the content below

      {profile}
       """)
                        .param("profile", profile.getProfileAsString()))
            .call()
            .content();

    String result =
        bio
            + "\n\n-------\n\n"
            + "Characters: %s ".formatted(bio.length())
            + "Words: %s".formatted(bio.split("\\s+").length);

    return result;
  }

  @GetMapping("/flow")
  public String agenticFlow() {
    LinkedProfile profile = new LinkedProfile(this.profile);
    List<String> stepResults = this.bioWriterAgent.writeBio(profile.getProfileAsString());
    String result = stepResults.stream().map(i -> "\n\n-------\n\n" + i).reduce("", String::concat);

    String bio = stepResults.get(stepResults.size() - 1);
    result +=
        "\n\n-------\n\n"
            + "Characters: %s ".formatted(bio.length())
            + "Words: %s".formatted(bio.split("\\s+").length);
    return result;
  }
}
