package com.example.chat_05.tool.return_direct;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class RestaurantSearch {

  private static final List<Restaurant> FAKE_DATABASE =
      List.of(
          new Restaurant(
              "La Bella Tavola",
              "Italian",
              6,
              "Cozy Italian spot with handmade pasta and great wine."),
          new Restaurant(
              "Casa di Roma",
              "Italian",
              10,
              "Upscale Roman-style trattoria known for wood-fired pizza."),
          new Restaurant(
              "Trattoria Napoli", "Italian", 4, "Family-friendly venue with Neapolitan classics."),
          new Restaurant("Sakura Garden", "Japanese", 8, "Elegant sushi and ramen bar."),
          new Restaurant(
              "Bombay Flame", "Indian", 12, "Modern Indian cuisine with traditional spice."));

  @Tool(
      description = "Search for restaurants that match the cuisine and party size",
      returnDirect = true)
  public List<Restaurant> searchForRestaurantsIn(
      @ToolParam(description = "The date for the desired restaurant reservation in ISO format")
          LocalDate date,
      @ToolParam(description = "The time of day that the reservation is needed for in ISO format")
          LocalTime time,
      @ToolParam(description = "The type of cuisine served by the restaurant") String cuisine,
      @ToolParam(description = "The number of people that the reservation must accommodate")
          Integer partySize) {
    return FAKE_DATABASE.stream()
        .filter(r -> r.cuisine().equalsIgnoreCase(cuisine))
        .filter(r -> r.capacity() >= partySize)
        .collect(Collectors.toList());
  }
}
