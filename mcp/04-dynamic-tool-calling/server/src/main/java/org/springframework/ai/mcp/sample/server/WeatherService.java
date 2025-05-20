/*
 * Copyright 2025 - 2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.ai.mcp.sample.server;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WeatherService {

  private final RestClient restClient;

  public WeatherService() {
    this.restClient =
        RestClient.builder()
            .defaultHeader("Accept", "application/geo+json")
            .defaultHeader("User-Agent", "WeatherApiClient/1.0 (your@email.com)")
            .build();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record WeatherResponse(@JsonProperty("current") Current current) { // @formatter:off
    public record Current(
        @JsonProperty("time") LocalDateTime time,
        @JsonProperty("interval") int interval,
        @JsonProperty("temperature_2m") double temperature_2m) {}
  } // @formatter:on

  @Tool(description = "Get the temperature (in celsius) for a specific location") // @formatter:off
  public WeatherResponse weatherForecast(
      @ToolParam(description = "The location latitude") double latitude,
      @ToolParam(description = "The location longitude") double longitude,
      ToolContext toolContext) { // @formatter:on

    WeatherResponse weatherResponse =
        restClient
            .get()
            .uri(
                "https://api.open-meteo.com/v1/forecast?latitude={latitude}&longitude={longitude}&current=temperature_2m",
                latitude,
                longitude)
            .retrieve()
            .body(WeatherResponse.class);

    return weatherResponse;
  }
}
