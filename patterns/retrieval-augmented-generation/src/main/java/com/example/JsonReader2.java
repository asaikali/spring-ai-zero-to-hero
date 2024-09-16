/*
 * Copyright 2024 - 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.StreamSupport;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.EmptyJsonMetadataGenerator;
import org.springframework.ai.reader.JsonMetadataGenerator;
import org.springframework.core.io.Resource;

/**
 * Temporal replacement of a bogus JsonReader in 1.0.0-M2.
 * https://github.com/spring-projects/spring-ai/issues/1365
 *
 * <p>The 1.0.0-SNAPSHOT will have a proper JsonReader.
 */
public class JsonReader2 implements DocumentReader {

  private Resource resource;

  private JsonMetadataGenerator jsonMetadataGenerator;

  private final ObjectMapper objectMapper = new ObjectMapper();

  /** The key from the JSON that we will use as the text to parse into the Document text */
  private List<String> jsonKeysToUse;

  public JsonReader2(Resource resource) {
    this(resource, new ArrayList<>().toArray(new String[0]));
  }

  public JsonReader2(Resource resource, String... jsonKeysToUse) {
    this(resource, new EmptyJsonMetadataGenerator(), jsonKeysToUse);
  }

  public JsonReader2(
      Resource resource, JsonMetadataGenerator jsonMetadataGenerator, String... jsonKeysToUse) {
    Objects.requireNonNull(jsonKeysToUse, "keys must not be null");
    Objects.requireNonNull(jsonMetadataGenerator, "jsonMetadataGenerator must not be null");
    Objects.requireNonNull(resource, "The Spring Resource must not be null");
    this.resource = resource;
    this.jsonMetadataGenerator = jsonMetadataGenerator;
    this.jsonKeysToUse = List.of(jsonKeysToUse);
  }

  @Override
  public List<Document> get() {
    try {
      JsonNode rootNode = objectMapper.readTree(this.resource.getInputStream());

      if (rootNode.isArray()) {
        return StreamSupport.stream(rootNode.spliterator(), true)
            .map(jsonNode -> parseJsonNode(jsonNode, objectMapper))
            .toList();
      } else {
        return Collections.singletonList(parseJsonNode(rootNode, objectMapper));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Document parseJsonNode(JsonNode jsonNode, ObjectMapper objectMapper) {
    Map<String, Object> item =
        objectMapper.convertValue(jsonNode, new TypeReference<Map<String, Object>>() {});
    StringBuffer sb = new StringBuffer();

    jsonKeysToUse.parallelStream()
        .filter(item::containsKey)
        .forEach(
            key -> {
              sb.append(key).append(": ").append(item.get(key)).append(System.lineSeparator());
            });

    Map<String, Object> metadata = this.jsonMetadataGenerator.generate(item);
    String content = sb.isEmpty() ? item.toString() : sb.toString();
    return new Document(content, metadata);
  }
}
