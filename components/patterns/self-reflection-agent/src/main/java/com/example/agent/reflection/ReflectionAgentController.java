package com.example.agent.reflection;

import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reflection/bio/")
public class ReflectionAgentController {

  private final ChatClient chatClient;
  private final SelfReflectionAgent selfReflectionAgent;

  @Value("classpath:/info/Profile.pdf")
  private Resource profile;

  @Autowired
  public ReflectionAgentController(ChatClient.Builder builder) {
    this.chatClient = builder.build();
    this.selfReflectionAgent = new SelfReflectionAgent(builder);
  }

  @GetMapping("/oneshot")
  public String oneShot() {

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

  @GetMapping("/agent")
  public String agent() {
    LinkedProfile profile = new LinkedProfile(this.profile);
    List<String> bios = selfReflectionAgent.write(profile.getProfileAsString(), 3);

    String result = "";
    for (int i = 0; i < bios.size(); i++) {
      String bio = bios.get(i);
      result +=
          """

            ------- Iteration %d -------
            %s

            Characters: %d
            Words: %d
           -----------------------------
            """
              .formatted(i + 1, bio, bio.length(), bio.split("\\s+").length);
    }

    return result;
  }
}
