package com.stockheap.weather.service.weather;

import com.stockheap.weather.service.external_weather.dto.WeatherDataAndResponseStatusDTO;
import reactor.core.publisher.Mono;

public interface WeatherMethods {

    Mono<WeatherDataAndResponseStatusDTO> getCurrentWeather(String zip, String country);

    Mono<WeatherDataAndResponseStatusDTO> getExtendedWeather(String zip, String country) ;
}
