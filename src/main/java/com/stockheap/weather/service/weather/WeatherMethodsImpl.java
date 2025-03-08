package com.stockheap.weather.service.weather;

import com.stockheap.weather.WeatherConstants;
import com.stockheap.weather.service.external_weather.common.ExternalWeatherMethods;
import com.stockheap.weather.service.external_weather.dto.WeatherDataAndResponseStatusDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@AllArgsConstructor
@Service
public class WeatherMethodsImpl implements WeatherMethods {

    @Autowired
    private ExternalWeatherMethods externalWeatherMethods;


    @Cacheable(unless="#result == null or #result.statusCode != 200 or #result.current == null", value = WeatherConstants.CacheNames.WEATHER_FORECAST_CACHE, key = "'CACHE_ZIP_PREFIX_CURRENT_' + #zip")
    public Mono<WeatherDataAndResponseStatusDTO> getCurrentWeather(String zip, String country) {
        return Mono.fromCallable(() -> externalWeatherMethods.getCurrentWeather(zip, country))
                .flatMap(mono -> mono)
                 .filter(value -> value != null &&
                        value.getCurrent() != null &&
                        value.getStatusCode() == 200)
                .cache();


    }

    @Cacheable(unless="#result == null or #result.statusCode != 200 or #result.extended == null or #result.extended.size() == 0", value = WeatherConstants.CacheNames.WEATHER_FORECAST_CACHE, key = "'CACHE_ZIP_PREFIX_EXT_' + #zip")
    public Mono<WeatherDataAndResponseStatusDTO> getExtendedWeather(String zip, String country) {
        return Mono.fromCallable(() -> externalWeatherMethods.getExtendedWeather(zip, country)).flatMap(mono -> mono)
                .filter(value -> value != null &&
                        value.getExtended() != null &&
                        value.getExtended().size() > 0 &&
                        value.getStatusCode() == 200)
                .cache();
    }


}
