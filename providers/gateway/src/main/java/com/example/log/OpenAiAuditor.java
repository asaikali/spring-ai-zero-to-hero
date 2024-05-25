package com.example.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OpenAiAuditor implements Auditor {
  Logger logger = LoggerFactory.getLogger(OpenAiAuditor.class);
  private final ObjectMapper objectMapper;
  private final ObjectWriter objectWriter;

  OpenAiAuditor() {
    this.objectMapper = new ObjectMapper();
    this.objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
  }

  @Override
  public void log(AuditLogEntry auditEntry) {
    StringBuilder message = new StringBuilder("\n\n");
    message.append("Entry ID: ").append(auditEntry.getId()).append("\n\n");

    var request = auditEntry.getRequest();

    // the method
    message
        .append(request.getMethod())
        .append(" ")
        .append(request.getDestinationUri())
        .append("\n");

    // print request headers
    //    for (var header : auditEntry.getRequest().getHeaders().keySet()) {
    //      message.append(header).append(":
    // ").append(request.getHeaders().get(header)).append("\n");
    //    }

    // print the request body
    try {
      var body = objectMapper.readValue(request.getBody(), Object.class);
      var pretty = this.objectWriter.writeValueAsString(body);
      message.append(pretty).append("\n");
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    // print response headers
    var response = auditEntry.getResponse();
    //    for (var header : response.getHeaders().keySet()) {
    //      message.append(header).append(":
    // ").append(response.getHeaders().get(header)).append("\n");
    //    }

    // print the response body
    try {
      var body = objectMapper.readValue(response.getBody(), Object.class);
      var pretty = this.objectWriter.writeValueAsString(body);
      message.append("\nResponse Body\n\n").append(pretty).append("\n");
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }


    logger.info(message.toString());
  }
}
