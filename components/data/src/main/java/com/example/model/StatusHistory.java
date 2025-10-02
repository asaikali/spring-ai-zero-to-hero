// StatusHistory.java
package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class StatusHistory {
  private String status;

  @JsonProperty("entered_at")
  private LocalDateTime enteredAt;

  // getters & setters
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDateTime getEnteredAt() {
    return enteredAt;
  }

  public void setEnteredAt(LocalDateTime enteredAt) {
    this.enteredAt = enteredAt;
  }
}
