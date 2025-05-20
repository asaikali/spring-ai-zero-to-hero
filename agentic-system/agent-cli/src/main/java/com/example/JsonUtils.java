package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
  public static String toPrettyJson(Object obj) {
    try {
      return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    } catch (Exception e) {
      return "[ERROR] Failed to format JSON: " + e.getMessage();
    }
  }
}
