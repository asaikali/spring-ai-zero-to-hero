package com.example.chat_05.tool.function;

import java.time.LocalDateTime;

class WeatherResponse {

  private String city;
  private double temperature;
  private String weatherCondition;
  private int humidity;
  private double windSpeed;
  private String windDirection;
  private LocalDateTime timeStamp;

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public double getTemperature() {
    return temperature;
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }

  public String getWeatherCondition() {
    return weatherCondition;
  }

  public void setWeatherCondition(String weatherCondition) {
    this.weatherCondition = weatherCondition;
  }

  public int getHumidity() {
    return humidity;
  }

  public void setHumidity(int humidity) {
    this.humidity = humidity;
  }

  public double getWindSpeed() {
    return windSpeed;
  }

  public void setWindSpeed(double windSpeed) {
    this.windSpeed = windSpeed;
  }

  public String getWindDirection() {
    return windDirection;
  }

  public void setWindDirection(String windDirection) {
    this.windDirection = windDirection;
  }

  public LocalDateTime getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(LocalDateTime timeStamp) {
    this.timeStamp = timeStamp;
  }
}
