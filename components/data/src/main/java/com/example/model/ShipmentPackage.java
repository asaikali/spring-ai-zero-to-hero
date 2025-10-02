// ShipmentPackage.java
package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public class ShipmentPackage {
  @JsonProperty("package_number")
  private String packageNumber;

  @JsonProperty("tracking_number")
  private String trackingNumber;

  private String carrier;
  private List<PackageContent> contents;

  @JsonProperty("shipped_at")
  private LocalDateTime shippedAt;

  @JsonProperty("delivered_at")
  private LocalDateTime deliveredAt;

  public String getPackageNumber() {
    return packageNumber;
  }

  public ShipmentPackage setPackageNumber(String packageNumber) {
    this.packageNumber = packageNumber;
    return this;
  }

  public String getTrackingNumber() {
    return trackingNumber;
  }

  public ShipmentPackage setTrackingNumber(String trackingNumber) {
    this.trackingNumber = trackingNumber;
    return this;
  }

  public String getCarrier() {
    return carrier;
  }

  public ShipmentPackage setCarrier(String carrier) {
    this.carrier = carrier;
    return this;
  }

  public List<PackageContent> getContents() {
    return contents;
  }

  public ShipmentPackage setContents(List<PackageContent> contents) {
    this.contents = contents;
    return this;
  }

  public LocalDateTime getShippedAt() {
    return shippedAt;
  }

  public ShipmentPackage setShippedAt(LocalDateTime shippedAt) {
    this.shippedAt = shippedAt;
    return this;
  }

  public LocalDateTime getDeliveredAt() {
    return deliveredAt;
  }

  public ShipmentPackage setDeliveredAt(LocalDateTime deliveredAt) {
    this.deliveredAt = deliveredAt;
    return this;
  }
}
