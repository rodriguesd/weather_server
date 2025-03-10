package com.stockheap.weather.service.common;


import reactor.core.publisher.Mono;


/**
 * <p>
        This interface is generic and is used to integrate with external weather services
 * </p>
 */

public interface ExternalWeatherMethods<T, U> {

    Mono<T> getCurrentWeather(String zip, String country);
    Mono<U> getExtendedWeather(String zip, String country);
}
