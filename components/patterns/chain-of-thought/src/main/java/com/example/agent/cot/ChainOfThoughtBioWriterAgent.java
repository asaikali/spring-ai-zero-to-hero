package com.example.agent.cot;

import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChainOfThoughtBioWriterAgent {

  private ChatClient chatClient;

  public ChainOfThoughtBioWriterAgent(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  public List<String> writeBio(String profile) {

    String outline =
        this.chatClient
            .prompt()
            .user(
                u ->
                    u.text(
                        """
         Create an outline for a professional biography that can be used in a speakers
         directory for a conference.
         """))
            .call()
            .content();

    String bioDraft1 =
        this.chatClient
            .prompt()
            .user(
                u ->
                    u.text(
                            """
        Use the outline in <outline> section to create a biography based on the
        LinkedIn profile in the <profile></profile> section.

        <outline>
        {outline}
        </outline>

        <profile>
        {profile}
        </profile>
        """)
                        .param("outline", outline)
                        .param("profile", profile))
            .call()
            .content();

    String bioDraft2 =
        this.chatClient
            .prompt()
            .user(
                u ->
                    u.text(
                            """
         Turn the biography below into a single paragraph suitable for use
         in an important document.
         ---
        {bio}
        """)
                        .param("bio", bioDraft1))
            .call()
            .content();

    String bioDraft3 =
        this.chatClient
            .prompt()
            .system(
                """
            You are a friendly expert writer, who helps professionals create
            impactful biographies, that is easy to understand, exciting,
            and show the brilliance of the professional. Your take pride
            in the short memorable biographies you come up with.
            """)
            .user(
                u ->
                    u.text(
                            """
            Evaluate the quality of the biography below and make it
            better, make sure it is a single paragraph, with a maximum
            of 200 words only return the improved biography and nothing else.
            The biography will be used in a very important conference, contact
            information is not important.
            ---
            {bio}
            """)
                        .param("bio", bioDraft2))
            .call()
            .content();

    String critique =
        this.chatClient
            .prompt()
            .system(
                """
            You are an expert marketer who helps clients improve the quality
            of thier communications. Your goal is to get rid of all fluff and
            focus on what truly matters for the target audience.
            """)
            .user(
                u ->
                    u.text(
                            """
            Analyze the biography in the <bio></bio> section below, and identify
            issues in the biography, and suggested improvements. The audience
            for this biography are busy professionals who are peers of the subject
            . Only produce the suggestions do not return an improved biography.
            <bio>
            {bio}
            </bio>
            """)
                        .param("bio", bioDraft3))
            .call()
            .content();

    String finalBio =
        this.chatClient
            .prompt()
            .system(
                """
            You are communications experts who makes written biographies
            better so that they appeal to peers from the same industry as
            the subject of the biography.
            """)
            .user(
                u ->
                    u.text(
                            """
            Below is a professional biography in the <bio></bio> section,
            a critique from an expert on the biography in the <critique></critique> section
            the linkedIn profile of the subject is in the <profile></profile>
            Improve the biography so that is a single paragraph with a max of 125 words,
            that appeals to peer professional in the same industry, make sure to validate it against the
            LinkedIn profile. return only the improved bio with no other commentary.

            <bio>
            {bio}
            </bio>

            <critique>
            {critique}
            </critique>

            <profile>
            {profile}
            </profile>
            """)
                        .param("bio", bioDraft3)
                        .param("critique", critique)
                        .param("profile", profile))
            .call()
            .content();

    return List.of(outline, bioDraft1, bioDraft2, bioDraft3, critique, finalBio);
  }
}
