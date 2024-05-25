package com.example.log;

import java.time.LocalDateTime;
import java.util.Map;

public class RequestLogEntry {

  private LocalDateTime timestamp;
  private String method;
  private String originalUri;
  private String destinationUri;
  private String body;
  private Map<String, String> headers;

  public RequestLogEntry() {
    this.timestamp = LocalDateTime.now();
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public RequestLogEntry setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  public String getMethod() {
    return method;
  }

  public RequestLogEntry setMethod(String method) {
    this.method = method;
    return this;
  }

  public String getOriginalUri() {
    return originalUri;
  }

  public RequestLogEntry setOriginalUri(String originalUri) {
    this.originalUri = originalUri;
    return this;
  }

  public String getDestinationUri() {
    return destinationUri;
  }

  public RequestLogEntry setDestinationUri(String destinationUri) {
    this.destinationUri = destinationUri;
    return this;
  }

  public String getBody() {
    return body;
  }

  public RequestLogEntry setBody(String body) {
    this.body = body;
    return this;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public RequestLogEntry setHeaders(Map<String, String> headers) {
    this.headers = headers;
    return this;
  }
}
