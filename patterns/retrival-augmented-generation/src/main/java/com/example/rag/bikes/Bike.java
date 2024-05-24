package com.example.rag.bikes;

import java.util.List;

public class Bike {
  private String name;
  private Double price;
  private String shortDescription;
  private String description;
  private List<String> tags;
  private Double range;
  private String batteryCapacity;
  private Double speed;
  private Integer gears;

  public String getName() {
    return name;
  }

  public Bike setName(String name) {
    this.name = name;
    return this;
  }

  public Double getPrice() {
    return price;
  }

  public Bike setPrice(Double price) {
    this.price = price;
    return this;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public Bike setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Bike setDescription(String description) {
    this.description = description;
    return this;
  }

  public List<String> getTags() {
    return tags;
  }

  public Bike setTags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public Double getRange() {
    return range;
  }

  public Bike setRange(Double range) {
    this.range = range;
    return this;
  }

  public String getBatteryCapacity() {
    return batteryCapacity;
  }

  public Bike setBatteryCapacity(String batteryCapacity) {
    this.batteryCapacity = batteryCapacity;
    return this;
  }

  public Double getSpeed() {
    return speed;
  }

  public Bike setSpeed(Double speed) {
    this.speed = speed;
    return this;
  }

  public Integer getGears() {
    return gears;
  }

  public Bike setGears(Integer gears) {
    this.gears = gears;
    return this;
  }
}
