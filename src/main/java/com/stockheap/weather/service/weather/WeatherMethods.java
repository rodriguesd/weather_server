package com.stockheap.weather.service.weather;

import com.stockheap.weather.service.common.dto.CurrentWeatherAndResponseStatusDTO;
import com.stockheap.weather.service.common.dto.ExtendedWeatherAndResponseStatusDTO;
import reactor.core.publisher.Mono;

public interface WeatherMethods {

    Mono<CurrentWeatherAndResponseStatusDTO> getCurrentWeather(String zip, String country);

    Mono<ExtendedWeatherAndResponseStatusDTO> getExtendedWeather(String zip, String country) ;
}
