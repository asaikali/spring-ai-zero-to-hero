package com.example.embed_03;

import com.example.data.DataFiles;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/embed/03")
public class EmbeddingRequestController {

  private final EmbeddingModel embeddingModel;
  private final String shakespeareWorks;
  private final DataFiles dataFiles;

  public EmbeddingRequestController(EmbeddingModel embeddingModel, DataFiles dataFiles)
      throws IOException {
    this.embeddingModel = embeddingModel;
    this.dataFiles = dataFiles;
    this.shakespeareWorks =
        this.dataFiles.getShakespeareWorksResource().getContentAsString(StandardCharsets.UTF_8);
  }

  @GetMapping("big")
  public String bigFile() {
    try {
      float[] embedding = embeddingModel.embed(shakespeareWorks);
      return """
           Success document of length %s characters, was embedded into
           1 vector with dimension %s
          """
          .formatted(shakespeareWorks.length(), embedding.length);
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
    List<String> chunks =
        tokenTextSplitter.split(new Document(shakespeareWorks)).stream()
            .map(d -> d.getContent())
            .toList();
    // for demo purposes we will only compute the
    // embeddings of the first 3 chunks, since we have to pay
    // per token when we call the LLM
    List<float[]> embeddings = this.embeddingModel.embed(chunks.subList(0, 3));

    return """
           file split into %s chunks %s embeddings created
           because we don't want to waste money by embedding every chunk
        """
        .formatted(chunks.size(), embeddings.size());
  }
}
