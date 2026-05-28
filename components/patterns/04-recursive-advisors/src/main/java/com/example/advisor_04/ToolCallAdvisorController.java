package com.example.advisor_04;

import com.example.chat_05.tool.annotations.TimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.ToolCallAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Demonstrates {@link ToolCallAdvisor} — new in Spring AI 2.0.
 *
 * <p>By default Spring AI executes tools <em>inside</em> the model loop (internal execution). When
 * {@code ToolCallAdvisor} is added to the advisor chain, tool execution moves into the chain
 * instead. This allows other advisors to observe, log, or intercept every tool invocation —
 * important for production observability.
 *
 * <p>Endpoints:
 *
 * <ul>
 *   <li>GET /advisor/04/internal — tool runs inside the model (default Spring AI 1.x behaviour)
 *   <li>GET /advisor/04/advisor-chain — tool runs via ToolCallAdvisor in the advisor chain (new in
 *       2.0)
 * </ul>
 */
@RestController
@RequestMapping("/advisor/04")
public class ToolCallAdvisorController {

  /** Default client: tool execution happens inside the ChatModel (Spring AI 1.x behaviour). */
  private final ChatClient defaultClient;

  /**
   * Advisor-chain client: ToolCallAdvisor moves tool execution into the advisor chain, enabling
   * other advisors to observe every tool invocation.
   */
  private final ChatClient advisorClient;

  public ToolCallAdvisorController(ChatClient.Builder builder, ChatModel model) {
    this.defaultClient = builder.build();
    this.advisorClient =
        ChatClient.builder(model).defaultAdvisors(ToolCallAdvisor.builder().build()).build();
  }

  /**
   * Tool runs inside the ChatModel — no visibility from the advisor chain.
   *
   * <p>Example: GET /advisor/04/internal?q=What+time+is+it+in+Tokyo
   */
  @GetMapping("/internal")
  public String withInternalExecution(
      @RequestParam(defaultValue = "What time is it in Tokyo?") String q) {
    return defaultClient.prompt().tools(new TimeTools()).user(q).call().content();
  }

  /**
   * Tool runs via {@link ToolCallAdvisor} in the advisor chain. Any advisor added after
   * ToolCallAdvisor can observe the tool call results.
   *
   * <p>Example: GET /advisor/04/advisor-chain?q=What+time+is+it+in+Tokyo
   */
  @GetMapping("/advisor-chain")
  public String withAdvisorChain(
      @RequestParam(defaultValue = "What time is it in Tokyo?") String q) {
    return advisorClient.prompt().tools(new TimeTools()).user(q).call().content();
  }
}
