package com.example;

import com.example.command.agent.AgentContext;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class AgentPromptProvider implements PromptProvider {

  private final AgentContext agentContext;

  public AgentPromptProvider(AgentContext agentContext) {
    this.agentContext = agentContext;
  }

  @Override
  public AttributedString getPrompt() {
    String agent = agentContext.getCurrentAgentId();

    StringBuilder sb = new StringBuilder("agent");
    if (agent != null) {
      sb.append("@").append(agent); // fallback if target missing
    }
    sb.append("> ");

    return new AttributedString(
        sb.toString(), AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
  }
}
