package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@CommandScan
@SpringBootApplication
public class ModelDirectedLoopCliApplication {
  public static void main(String[] args) {
    SpringApplication.run(ModelDirectedLoopCliApplication.class, args);
  }
}
