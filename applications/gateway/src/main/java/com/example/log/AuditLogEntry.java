package com.example.log;

import java.util.UUID;

public class AuditLogEntry {
  public static final String AUDIT_LOG_ENTRY = "ai-audit-log-entry";
  private UUID id;
  private RequestLogEntry request;
  private ResponseLogEntry response;

  public AuditLogEntry() {
    this.id = UUID.randomUUID();
    this.request = new RequestLogEntry();
  }

  public UUID getId() {
    return id;
  }

  public AuditLogEntry setId(UUID id) {
    this.id = id;
    return this;
  }

  public RequestLogEntry getRequest() {
    return request;
  }

  public AuditLogEntry setRequest(RequestLogEntry request) {
    this.request = request;
    return this;
  }

  public ResponseLogEntry getResponse() {
    return response;
  }

  public AuditLogEntry setResponse(ResponseLogEntry response) {
    this.response = response;
    return this;
  }
}
