package com.example.embed_02;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/embed/02")
public class EmbeddingRequestController {

  private final EmbeddingClient embeddingClient;
  private final String shakespeareWorks;

  public EmbeddingRequestController(EmbeddingClient embeddingClient) throws IOException {
    this.embeddingClient = embeddingClient;
    this.shakespeareWorks =
        new ClassPathResource("/data/books/Shakespeare.txt")
            .getContentAsString(StandardCharsets.UTF_8);
  }

  @GetMapping("big")
  public String bigFile() {
    try {
      List<Double> embedding = embeddingClient.embed(shakespeareWorks);
      return """
                    Success document of length %s characters, was embedded into
                    1 vector with dimension %s
                   """
          .formatted(shakespeareWorks.length(), embedding.size());
    } catch (Exception e) {
      return """
                    Error: document of length %s could not be embedded
                    Exception Message: %s
                    """
          .formatted(shakespeareWorks.length(), e.getMessage());
    }
  }

  @GetMapping("chunk")
  public String chunkFile() {
    TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
    List<String> chunks = tokenTextSplitter.split(shakespeareWorks, 8000);
    // for demo purposes we will only compute the
    // embeddings of the first 3 chunks, since we have to pay
    // per token when we call the LLM
    List<List<Double>> embeddings = this.embeddingClient.embed(chunks.subList(0, 3));

    return """
                   file split into %s chunks %s embeddings created
                   because we don't want to waste money by embedding every chunk
                """
        .formatted(chunks.size(), embeddings.size());
  }
}
