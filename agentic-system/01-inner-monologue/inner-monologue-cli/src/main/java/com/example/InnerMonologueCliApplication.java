package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@CommandScan
@SpringBootApplication
public class InnerMonologueCliApplication {
  public static void main(String[] args) {
    SpringApplication.run(InnerMonologueCliApplication.class, args);
  }
}
