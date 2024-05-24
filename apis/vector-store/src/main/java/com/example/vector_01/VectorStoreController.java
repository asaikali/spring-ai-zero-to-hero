package com.example.vector_01;

import com.example.data.DataFiles;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vector/01")
public class VectorStoreController {
  private final Logger logger = LoggerFactory.getLogger(VectorStoreController.class);

  private final DataFiles dataFiles;
  private final VectorStore bikeVectorStore;

  public VectorStoreController(EmbeddingClient embeddingClient, DataFiles dataFiles)
      throws IOException {
    this.bikeVectorStore = new SimpleVectorStore(embeddingClient);
    this.dataFiles = dataFiles;
  }

  @GetMapping("/load")
  public String load() throws IOException {
    // turn the json specs file into a document per bike
    DocumentReader reader =
        new JsonReader(
            this.dataFiles.getBikesResource(), "name", "price", "shortDescription", "description");
    List<Document> documents = reader.get();

    // add the documents to the vector store
    this.bikeVectorStore.add(documents);

    var file = File.createTempFile("bike_vector_store", ".json");
    ((SimpleVectorStore) this.bikeVectorStore).save(file);
    logger.info("vector store contents written to {}", file.getAbsolutePath());

    return "vector store loaded with %s documents, file saved to %s "
        .formatted(documents.size(), file.getAbsolutePath());
  }

  @GetMapping("query")
  public List<String> query(
      @RequestParam(value = "topic", defaultValue = "Which bikes have extra long range")
          String topic) {

    // search the vector store for the top 4 bikes that match the query
    List<Document> topMatches = this.bikeVectorStore.similaritySearch(topic);

    return topMatches.stream().map(document -> document.getContent()).toList();
  }
}
