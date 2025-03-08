package com.stockheap.weather.service.external_weather.common;


import com.stockheap.weather.service.external_weather.dto.CurrentWeatherAndResponseStatusDTO;
import com.stockheap.weather.service.external_weather.dto.ExtendedWeatherAndResponseStatusDTO;
import reactor.core.publisher.Mono;

public interface ExternalWeatherMethods {

    Mono<CurrentWeatherAndResponseStatusDTO> getCurrentWeather(String zip, String country);
    Mono<ExtendedWeatherAndResponseStatusDTO> getExtendedWeather(String zip, String country);
}
