package org.springframework.ai.mcp.sample.server;

import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.server.McpSyncServer;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.McpToolUtils;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ServerApplication {

  private static final Logger logger = LoggerFactory.getLogger(ServerApplication.class);

  CountDownLatch latch = new CountDownLatch(1);

  public static void main(String[] args) {
    SpringApplication.run(ServerApplication.class, args);
  }

  @GetMapping("/updateTools")
  public String greeting() {
    latch.countDown();
    return "Update signal received!";
  }

  @Bean
  public ToolCallbackProvider weatherTools(WeatherService weatherService) {
    return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
  }

  @Bean
  public CommandLineRunner commandRunner(McpSyncServer mcpSyncServer) {

    return args -> {
      logger.info("Server: " + mcpSyncServer.getServerInfo());

      latch.await();

      List<SyncToolSpecification> newTools =
          McpToolUtils.toSyncToolSpecifications(ToolCallbacks.from(new MathTools()));

      for (SyncToolSpecification newTool : newTools) {
        logger.info("Add new tool: " + newTool);
        mcpSyncServer.addTool(newTool);
      }

      logger.info("Tools updated: ");
    };
  }
}
