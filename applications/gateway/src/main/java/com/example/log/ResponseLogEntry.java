package com.example.log;

import java.time.LocalDateTime;
import java.util.Map;

public class ResponseLogEntry {
  private LocalDateTime timestamp;
  private String body;
  private Map<String, String> headers;

  public ResponseLogEntry() {
    this.timestamp = LocalDateTime.now();
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public ResponseLogEntry setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  public String getBody() {
    return body;
  }

  public ResponseLogEntry setBody(String body) {
    this.body = body;
    return this;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public ResponseLogEntry setHeaders(Map<String, String> headers) {
    this.headers = headers;
    return this;
  }
}
