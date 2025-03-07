package com.stockheap.weather.service.external_weather.common;


import com.stockheap.weather.service.external_weather.dto.WeatherDataAndResponseStatusDTO;
import reactor.core.publisher.Mono;

public interface ExternalWeatherMethods {

    Mono<WeatherDataAndResponseStatusDTO> getCurrentWeather(String zip, String country);
    Mono<WeatherDataAndResponseStatusDTO> getExtendedWeather(String zip, String country);
}
