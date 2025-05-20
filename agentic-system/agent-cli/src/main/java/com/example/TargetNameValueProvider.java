package com.example;

import java.util.List;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProvider;
import org.springframework.stereotype.Component;

@Component
public class TargetNameValueProvider implements ValueProvider {

  private final TargetContext targetContext;

  public TargetNameValueProvider(TargetContext targetContext) {
    this.targetContext = targetContext;
  }

  @Override
  public List<CompletionProposal> complete(CompletionContext context) {
    return targetContext.getTargets().keySet().stream().map(CompletionProposal::new).toList();
  }
}
