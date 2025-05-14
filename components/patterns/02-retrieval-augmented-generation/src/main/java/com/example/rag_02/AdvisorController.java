package com.example.rag_02;

import com.example.JsonReader2;
import com.example.data.DataFiles;
import java.io.IOException;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
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
    this.chatClient =
        builder
            .defaultSystem(
                """
				                You are a helpful assistant at an e-bike store. Your job is to answer
				                customer questions about e-bikes that we are selling. The questions should
				                be answered based on the provided bike specifications. If you don't know
				                the answer politely tell the customer you don't know the answer, then ask
				                the customer a followup question to try and clarify the question they are
				                asking.
				""")
            .build();
  }

  private static final String USER_TEXT_ADVISE =
      """

      Given the context information below , surrounded ---------------------, and provided
      history information and not prior knowledge, reply to the user comment.
      If the answer is not in the context, inform the user that you can't answer the question.

			---------------------
			{question_answer_context}
			---------------------
			""";

  @GetMapping("/load")
  public String load() throws IOException {
    // turn the json specs file into a document per bike
    DocumentReader reader =
        new JsonReader2(
            this.dataFiles.getBikesResource(), "name", "price", "shortDescription", "description");
    List<Document> documents = reader.get();

    // add the documents to the vector store
    this.vectorStore.add(documents);
    return "vector store loaded with %s documents".formatted(documents.size());
  }

  @GetMapping("query")
  public String query(
      @RequestParam(value = "topic", defaultValue = "Which bikes have extra long range? /n")
          String topic) {

    return this.chatClient
        .prompt()
        .advisors(
            QuestionAnswerAdvisor.builder(vectorStore)
                .promptTemplate(new PromptTemplate(USER_TEXT_ADVISE))
                .build())
        .user(topic)
        .call()
        .content();
  }
}
