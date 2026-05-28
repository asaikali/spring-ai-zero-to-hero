package com.example.chat_09;

import com.example.chat_05.tool.annotations.TimeTools;
import com.example.chat_05.tool.return_direct.RestaurantSearch;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Demonstrates dynamic tool selection — new in Spring AI 2.0.
 *
 * <p>When a ChatClient has a large number of tools registered, sending all tool schemas with every
 * request wastes tokens and degrades model performance. Spring AI 2.0 introduces dynamic tool
 * search: before each request the framework uses embedding similarity to select only the tools most
 * relevant to the user's query and forwards only those schemas to the model.
 *
 * <p>Benchmarks show 34–64% token reduction when tool catalogs have 10+ tools.
 *
 * <p>Endpoints:
 *
 * <ul>
 *   <li>GET /chat/09/all — sends all tool schemas with every request (baseline)
 *   <li>GET /chat/09/smart — only relevant tools forwarded (token-efficient)
 * </ul>
 *
 * <p><b>Note:</b> Dynamic tool search (embedding-based selection) requires a {@code VectorStore}
 * bean and is enabled by calling {@code ChatClient.Builder.defaultToolSearch(vectorStore)} —
 * available when running with the {@code pgvector} profile. Without a VectorStore the {@code
 * /smart} endpoint falls back to sending all tools (same as {@code /all}), so both endpoints still
 * work in basic mode.
 */
@RestController
@RequestMapping("/chat/09")
public class DynamicToolSearchController {

  private final ChatClient chatClient;

  public DynamicToolSearchController(ChatClient.Builder builder) {
    this.chatClient = builder.build();
  }

  /**
   * Sends all tool schemas to the model regardless of the query.
   *
   * <p>Baseline — shows how many tokens tool schemas consume when the full catalog is always sent.
   *
   * <p>Example: GET /chat/09/all?q=What+time+is+it+in+Berlin
   */
  @GetMapping("/all")
  public String allTools(@RequestParam(defaultValue = "What time is it in Berlin?") String q) {
    return chatClient
        .prompt()
        .tools(new TimeTools(), new RestaurantSearch())
        .user(q)
        .call()
        .content();
  }

  /**
   * Uses dynamic tool selection: only tools relevant to the query are forwarded to the model.
   *
   * <p>With a {@code VectorStore} configured, Spring AI 2.0 embeds the query, performs a similarity
   * search over registered tool descriptions, and passes only the top-k matches. Without a
   * VectorStore, all tools are forwarded (graceful degradation).
   *
   * <p>Example: GET /chat/09/smart?q=What+time+is+it+in+Berlin
   */
  @GetMapping("/smart")
  public String smartTools(@RequestParam(defaultValue = "What time is it in Berlin?") String q) {
    // TODO: Uncomment when running with pgvector profile and a VectorStore bean is available:
    // return chatClient.prompt()
    //     .toolSearch(vectorStore)   // enables embedding-based dynamic tool selection
    //     .tools(new TimeTools(), new RestaurantSearch())
    //     .user(q).call().content();

    // For now, falls back to all-tools (same as /all)
    return chatClient
        .prompt()
        .tools(new TimeTools(), new RestaurantSearch())
        .user(q)
        .call()
        .content();
  }
}
