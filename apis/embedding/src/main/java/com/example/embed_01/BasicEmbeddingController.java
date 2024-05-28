package com.example.embed_01;

import java.util.List;
import java.util.Map;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/embed/01")
public class BasicEmbeddingController {

  private final EmbeddingModel embeddingModel;

  public BasicEmbeddingController(EmbeddingModel embeddingModel) {
    this.embeddingModel = embeddingModel;
  }

  @GetMapping("joke")
  public List<Double> getJoke() {
    return embeddingModel.embed("Tell me a joke");
  }

  @GetMapping("dimension")
  public Map<String, Object> getDimension() {

    return Map.of("dimension", embeddingModel.dimensions(), "type", embeddingModel.getClass());
  }
}
