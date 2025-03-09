package com.stockheap.weather.service.common;


import com.stockheap.weather.service.weather.dto.CurrentWeatherAndResponseStatusDTO;
import com.stockheap.weather.service.weather.dto.ExtendedWeatherAndResponseStatusDTO;
import reactor.core.publisher.Mono;


/**
 * <p>
        This interface is generic and is used to integrate with external weather services
 * </p>
 */

public interface ExternalWeatherMethods {

    Mono<CurrentWeatherAndResponseStatusDTO> getCurrentWeather(String zip, String country);
    Mono<ExtendedWeatherAndResponseStatusDTO> getExtendedWeather(String zip, String country);
}
