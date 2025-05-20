package com.example;

import com.example.command.agent.AgentContext;
import com.example.command.target.TargetContext;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.jline.PromptProvider;

@Configuration
public class AgentPromptConfig {

  private final AgentContext agentContext;
  private final TargetContext targetContext;

  public AgentPromptConfig(AgentContext agentContext, TargetContext targetContext) {
    this.agentContext = agentContext;
    this.targetContext = targetContext;
  }

  @Bean
  public PromptProvider promptProvider() {
    return () -> {
      String target = targetContext.getCurrentTargetName();
      String agent = agentContext.getCurrentAgentId();

      StringBuilder sb = new StringBuilder("agent");
      if (target != null) {
        sb.append("@").append(target);
        if (agent != null) {
          sb.append(":").append(agent);
        }
      } else if (agent != null) {
        sb.append("@_:").append(agent); // fallback if target missing
      }
      sb.append("> ");

      return new AttributedString(
          sb.toString(), AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
    };
  }
}
