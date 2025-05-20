package com.example;

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
      String promptText =
          agentContext.getCurrentAgentId() != null
              ? "agent@" + agentContext.getCurrentAgentId() + "> "
              : "agent> ";
      return new AttributedString(
          promptText, AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
    };
  }
}
