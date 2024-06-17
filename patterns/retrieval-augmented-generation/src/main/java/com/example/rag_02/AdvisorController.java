package com.example.rag_02;

import com.example.data.DataFiles;
import java.io.IOException;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rag/02")
public class AdvisorController {

  private final DataFiles dataFiles;
  private final VectorStore vectorStore;
  private final ChatClient chatClient;

  public AdvisorController(
      VectorStore vectorStore, DataFiles dataFiles, ChatClient.Builder builder) {
    this.dataFiles = dataFiles;
    this.vectorStore = vectorStore;
    this.chatClient = builder.build();
  }

  @GetMapping("/load")
  public String load() throws IOException {
    // turn the json specs file into a document per bike
    DocumentReader reader =
        new JsonReader(
            this.dataFiles.getBikesResource(), "name", "price", "shortDescription", "description");
    List<Document> documents = reader.get();

    // add the documents to the vector store
    this.vectorStore.add(documents);
    return "vector store loaded with %s documents".formatted(documents.size());
  }

  @GetMapping("query")
  public String query(
      @RequestParam(value = "topic", defaultValue = "Which bikes have extra long range")
          String topic) {

    return this.chatClient
        .prompt()
        .advisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()))
        .user(topic)
        .call()
        .content();
  }
}
