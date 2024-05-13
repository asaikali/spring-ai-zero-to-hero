package com.example.chat_05;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class WeatherFunctionConfiguration {

    @Bean
    @Description("Get the weather in location")
    public Function<CurrentWeatherRequest, CurrentWeatherResponse> weatherFunction(WeatherService  weatherService) {
        return request -> weatherService.getCurrentWeather(request.city());
    }
}
