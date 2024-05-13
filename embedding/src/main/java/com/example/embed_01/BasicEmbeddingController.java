package com.example.embed_01;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/embed/01")
public class BasicEmbeddingController {

    private final EmbeddingClient embeddingClient;

    public BasicEmbeddingController(EmbeddingClient embeddingClient) {
        this.embeddingClient = embeddingClient;
    }


    @GetMapping("joke")
    public List<Double> getJoke() {
        return embeddingClient.embed("Tell me a joke");
    }
}
