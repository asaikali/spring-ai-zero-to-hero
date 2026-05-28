package com.example.mem_04;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Demonstrates persistent JDBC-backed chat memory — new in Spring AI 2.0.
 *
 * <p>Unlike {@code InMemoryChatMemoryRepository} (used in mem_02 and mem_03), the {@link
 * JdbcChatMemoryRepository} persists conversations in a relational database. This means
 * conversations survive application restarts — essential for production use.
 *
 * <p>This controller only activates when a {@link JdbcChatMemoryRepository} bean is present, which
 * requires a {@code DataSource}. Enable by running with the {@code pgvector} profile:
 *
 * <pre>
 *   ./mvnw spring-boot:run -pl applications/provider-openai -Dspring.profiles.active=pgvector
 * </pre>
 *
 * <p>Add the following to your provider's {@code application.yaml} (pgvector profile):
 *
 * <pre>
 * spring:
 *   ai:
 *     chat:
 *       memory:
 *         repository:
 *           jdbc:
 *             initialize-schema: always
 * </pre>
 *
 * <p>Endpoints:
 *
 * <ul>
 *   <li>GET /mem/04/chat?message=Hi+my+name+is+Bob&amp;sessionId=s1
 *   <li>Restart the app, then: GET /mem/04/chat?message=What+is+my+name&amp;sessionId=s1
 *   <li>The model should still know "Bob" — proof the conversation was persisted.
 * </ul>
 */
@RestController
@RequestMapping("/mem/04")
@ConditionalOnBean(JdbcChatMemoryRepository.class)
public class JdbcChatMemoryController {

  private final ChatClient chatClient;

  public JdbcChatMemoryController(ChatClient.Builder builder, JdbcChatMemoryRepository repository) {
    ChatMemory memory =
        MessageWindowChatMemory.builder().chatMemoryRepository(repository).maxMessages(20).build();
    this.chatClient =
        builder.defaultAdvisors(MessageChatMemoryAdvisor.builder(memory).build()).build();
  }

  /** Chat with persistent memory keyed by {@code sessionId}. */
  @GetMapping("/chat")
  public String chat(
      @RequestParam String message, @RequestParam(defaultValue = "default") String sessionId) {
    return chatClient
        .prompt()
        .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId))
        .user(message)
        .call()
        .content();
  }
}
