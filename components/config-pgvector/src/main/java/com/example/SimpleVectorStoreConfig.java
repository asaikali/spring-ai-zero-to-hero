package com.example;

import org.springframework.ai.autoconfigure.vectorstore.pgvector.PgVectorStoreAutoConfiguration;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!pgvector")
@EnableAutoConfiguration(
    exclude = {
      PgVectorStoreAutoConfiguration.class,
      DataSourceAutoConfiguration.class,
      FlywayAutoConfiguration.class
    })
public class SimpleVectorStoreConfig {
  @Bean
  VectorStore vectorStore(EmbeddingModel embeddingModel) {

    return SimpleVectorStore.builder(embeddingModel).build();
  }
}
