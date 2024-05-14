package com.example.embed_04;

import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

@RestController
@RequestMapping("/embed/04")
public class VectorStoreController {
  private final Logger logger = LoggerFactory.getLogger(VectorStoreController.class);
  private final EmbeddingClient embeddingClient;


  @Value("classpath:/data/bikes/bikes.json")
  private Resource bikesResource;
  private final SimpleVectorStore bikeVectorStore;

  public VectorStoreController(EmbeddingClient embeddingClient) throws IOException {
    this.embeddingClient = embeddingClient;
    this.bikeVectorStore = new SimpleVectorStore(this.embeddingClient);
  }

  @GetMapping("bikes")
  public List<String> bikeJsonToDocs() throws IOException {

    // turn the json specs file into a document per bike
    DocumentReader reader =
        new JsonReader(bikesResource, "name", "price", "shortDescription", "description");
    List<Document> documents = reader.get();

    // add the documents to the vector store
    this.bikeVectorStore.add(documents);

    var file = File.createTempFile("bike_vector_store",".json");
    this.bikeVectorStore.save(file);
    logger.info("vector store contents written to {}",file.getAbsolutePath());


    // search the vector store for the top 4 bikes that match the query
    List<Document> topMatches = this.bikeVectorStore.similaritySearch("I have a long commute, which bike has the longest range?");

    return topMatches.stream().map(document -> document.getContent()).toList();
  }

}
