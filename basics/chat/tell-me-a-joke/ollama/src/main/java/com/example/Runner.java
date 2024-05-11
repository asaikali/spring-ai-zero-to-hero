package com.example;

import org.springframework.boot.CommandLineRunner;

public class Runner implements CommandLineRunner {
    private final JokeService jokeService;

    public Runner(JokeService jokeService) {
        this.jokeService = jokeService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(jokeService.getJoke());
    }
}
