package com.example.embed_01;

import java.util.List;
import java.util.Map;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.web.bind.annotation.*;

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

  @GetMapping("dimension")
  public Map<String, Object> getDimension() {

    return Map.of("dimension", embeddingClient.dimensions(), "type", embeddingClient.getClass());
  }
}
