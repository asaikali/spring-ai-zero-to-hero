package com.example;

import com.example.command.agent.AgentContext;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.jline.PromptProvider;

@Configuration
public class AgentPromptConfig {

  private final AgentContext agentContext;

  public AgentPromptConfig(AgentContext agentContext) {
    this.agentContext = agentContext;
  }

  @Bean
  public PromptProvider promptProvider() {
    return () -> {
      String agent = agentContext.getCurrentAgentId();

      StringBuilder sb = new StringBuilder("agent");
      if (agent != null) {
        sb.append("@_:").append(agent); // fallback if target missing
      }
      sb.append("> ");

      return new AttributedString(
          sb.toString(), AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
    };
  }
}
