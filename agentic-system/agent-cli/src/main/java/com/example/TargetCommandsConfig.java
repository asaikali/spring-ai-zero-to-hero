package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.shell.command.CommandRegistration;

// @Configuration
public class TargetCommandsConfig {

  @Bean
  public CommandRegistration targetListCommand(TargetContext targetCtx) {
    return CommandRegistration.builder()
        .command("target", "list")
        .description("List all available targets")
        .withTarget()
        .function(
            ctx -> {
              var systems = targetCtx.getTargets();
              if (systems.isEmpty()) {
                System.out.println("No targets available.");
              } else {
                systems.forEach((key, value) -> System.out.println(key + " -> " + value));
              }
              return null;
            })
        .and()
        .build();
  }

  @Bean
  public CommandRegistration targetUseCommand(
      TargetContext targetCtx, TargetNameValueProvider valueProvider) {
    return CommandRegistration.builder()
        .command("target", "use")
        .description("Set the current target")
        .withOption()
        .longNames("name")
        .required()
        .description("Target name to switch to")
        .type(String.class)
        .and()
        .withTarget()
        .function(
            ctx -> {
              String name = ctx.getOptionValue("name");
              var systems = targetCtx.getTargets();
              if (!systems.containsKey(name)) {
                System.out.println("[ERROR] No target with name: " + name);
              } else {
                targetCtx.setCurrentTargetName(name);
                System.out.println("Switched to target: " + name);
              }
              return null;
            })
        .and()
        .build();
  }

  @Bean
  public CommandRegistration targetStatusCommand(TargetContext targetCtx) {
    return CommandRegistration.builder()
        .command("target", "status")
        .description("Show the currently active target")
        .withTarget()
        .function(
            ctx -> {
              String current = targetCtx.getCurrentTargetName();
              if (current == null) {
                System.out.println("No active target.");
              } else {
                String url = targetCtx.getTargets().getOrDefault(current, "unknown URL");
                System.out.println("Current target: " + current);
                System.out.println("URL: " + url);
              }
              return null;
            })
        .and()
        .build();
  }

  @Bean
  public TargetNameValueProvider targetNameValueProvider(TargetContext targetCtx) {
    return new TargetNameValueProvider(targetCtx);
  }
}
