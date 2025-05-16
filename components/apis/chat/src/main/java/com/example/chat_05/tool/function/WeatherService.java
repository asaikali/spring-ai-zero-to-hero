package com.example.chat_05.tool.function;

import java.time.LocalDateTime;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
class WeatherService {

  private static final String[] CONDITIONS = {"Sunny", "Cloudy", "Rainy", "Partly Cloudy", "Snowy"};
  private static final String[] WIND_DIRECTIONS = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};

  public WeatherResponse getCurrentWeather(String city) {
    Random random = new Random();

    WeatherResponse weather = new WeatherResponse();
    weather.setCity(city);
    weather.setTemperature(
        roundToOneDecimalPlace(
            10
                + (30 - 10)
                    * random.nextDouble())); // Random temperature between 10 and 30, rounded to one
    // decimal place
    weather.setWeatherCondition(CONDITIONS[random.nextInt(CONDITIONS.length)]);
    weather.setHumidity(random.nextInt(101)); // Random humidity between 0 and 100%
    weather.setWindSpeed(
        roundToOneDecimalPlace(
            random.nextDouble()
                * 20)); // Random wind speed between 0 and 20 km/h, rounded to one decimal place
    weather.setWindDirection(WIND_DIRECTIONS[random.nextInt(WIND_DIRECTIONS.length)]);
    weather.setTimeStamp(LocalDateTime.now()); // Current timestamp

    return weather;
  }

  public String getWeatherReport(String city) {
    final WeatherResponse weather = this.getCurrentWeather(city);
    return String.format(
        """
                        Weather update for %s.
                        Currently, the skies are %s with the temperature at %.1f degrees.
                        The humidity stands at %d%%, and we've got winds moving at %.1f km/h, coming from the %s.""",
        weather.getCity(),
        weather.getWeatherCondition().toLowerCase(),
        weather.getTemperature(),
        weather.getHumidity(),
        weather.getWindSpeed(),
        weather.getWindDirection());
  }

  private double roundToOneDecimalPlace(double value) {
    return Math.round(value * 10.0) / 10.0;
  }
}
