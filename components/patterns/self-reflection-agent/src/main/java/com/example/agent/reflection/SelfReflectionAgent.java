package com.example.agent.reflection;

import java.util.ArrayList;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;

public class SelfReflectionAgent {

  private final Writer writer;
  private final Critic critic;

  protected SelfReflectionAgent(ChatClient.Builder clientBuilder) {
    this.writer = new Writer(clientBuilder);
    this.critic = new Critic(clientBuilder);
  }

  protected List<String> write(String linkedProfile, int iterations) {
    List<String> result = new ArrayList<>();
    String bio = this.writer.write(linkedProfile);
    result.add(bio);

    for (int i = 0; i < iterations - 1; i++) {
      String feedback = this.critic.provideFeedback(linkedProfile, bio);
      bio = this.writer.revise(linkedProfile, bio, feedback);
      result.add(bio);
    }

    return result;
  }

  private static class Writer {

    private final ChatClient chatClient;

    public Writer(ChatClient.Builder chatClientBuilder) {
      this.chatClient =
          chatClientBuilder
              .defaultSystem(
                  """
          You are a friendly expert writer, who helps professionals create
          impactful biographies, that is easy to understand, exciting,
          and show the brilliance of the professional. Your take pride
          in the short memorable biographies you come up with.

          The biography is written based on the professionals
          work experience provided to you for example a LinkedIn profile or
          Resume.

          The biography you write is targeted at peer professionals,
          in the same industry as the subject of the biography.


          You are also great at taking feedback about existing biography and
          using that feedback to improve an already written biography.
          """)
              .build();
    }

    public String write(String linkedProfile) {
      return this.chatClient
          .prompt()
          .user(
              userSpec ->
                  userSpec
                      .text(
                          """
    Write a one paragraph professional biography suitable for conference presentation based on the content below

    {profile}
     """)
                      .param("profile", linkedProfile))
          .call()
          .content();
    }

    public String revise(String linkedProfile, String biography, String feedback) {
      return this.chatClient
          .prompt()
          .user(
              u ->
                  u.text(
                          """
              Below is a professional biography in the <bio></bio> section,
              a critique from an expert on the biography in the <feedback></feedback> section
              the linkedIn profile of the subject is in the <profile></profile>
              Improve the biography so that is a single paragraph with a max of 125 words,
              that appeals to peer professional in the same industry, make sure to validate it against the
              LinkedIn profile. return only the improved bio with no other commentary.

              <bio>
              {bio}
              </bio>

              <feedback>
              {feedback}
              </feedback>

              <profile>
              {profile}
              </profile>
              """)
                      .param("bio", biography)
                      .param("feedback", feedback)
                      .param("profile", linkedProfile))
          .call()
          .content();
    }
  }

  private static class Critic {

    private final ChatClient chatClient;

    public Critic(ChatClient.Builder chatClientBuilder) {
      this.chatClient =
          chatClientBuilder
              .defaultSystem(
                  """
           You are an expert marketer who helps clients improve the quality
          of thier communications. Your goal is to get rid of all fluff and
          focus on what truly matters for the target audience. You provide
          clear feedback on how to make a written content more impactful for
          the target audience to help a writer accomplish thier objective.
          """)
              .build();
    }

    public String provideFeedback(String linkedProfile, String biography) {

      return this.chatClient
          .prompt()
          .user(
              u ->
                  u.text(
                          """
          Analyze the biography in the <bio></bio> section below, and identify
          issues in the biography, and suggested improvements. The audience
          for this biography are busy professionals who are peers of the subject
          . Only produce the suggestions do not return an improved biography.
          The biography is based on the LinkedIn profile in the <profile></profile>
          section make sure that the bio accurately reflects whats in the profile.
          <bio>
          {bio}
          </bio>

          <profile>
          {profile}
          </profile>
          """)
                      .param("bio", biography)
                      .param("profile", linkedProfile))
          .call()
          .content();
    }
  }
}
