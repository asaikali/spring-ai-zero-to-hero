package com.example;

import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Command(command = "chat", description = "Agentic Chat Commands")
public class ChatCommands {

    private final List<String> messages = new ArrayList<>();

    @Command(command = "start", description = "Start a new chat session")
    public void start() {
        messages.clear();
        messages.add("[SYSTEM] Welcome to the agentic test chat!");
        printChat();
    }

    @Command(command = "send", description = "Send a message to the agent")
    public void send(@Option(longNames = "text", required = true) String text) {
        messages.add("[USER] " + text);
        messages.add("[AGENT] Agent received: \"" + text + "\"");
        printChat();
    }

    @Command(command = "log", description = "Show the current chat log")
    public void log() {
        printChat();
    }

    private void printChat() {
        System.out.println("\n=== Agentic Chat ===");
        messages.forEach(System.out::println);
        System.out.println("====================\n");
    }
}
