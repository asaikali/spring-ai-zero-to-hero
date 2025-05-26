package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.stereotype.Component;

@Component
public class JsonUtils {

  private final ObjectWriter prettyWriter;

  public JsonUtils(ObjectMapper objectMapper) {
    this.prettyWriter = objectMapper.writerWithDefaultPrettyPrinter();
  }

  public String toPrettyJson(Object obj) {
    try {
      return prettyWriter.writeValueAsString(obj);
    } catch (Exception e) {
      return "[ERROR] Failed to format JSON: " + e.getMessage();
    }
  }
}
