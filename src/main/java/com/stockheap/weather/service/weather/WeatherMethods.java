package com.stockheap.weather.service.weather;

import com.stockheap.weather.data.common.dto.WeatherData;
import com.stockheap.weather.service.external_weather.dto.WeatherDataAndResponseStatusDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface WeatherMethods {

    Mono<WeatherDataAndResponseStatusDTO> getCurrentWeather(String zip, String country);

    Mono<WeatherDataAndResponseStatusDTO> getExtendedWeather(String zip, String country);
}
