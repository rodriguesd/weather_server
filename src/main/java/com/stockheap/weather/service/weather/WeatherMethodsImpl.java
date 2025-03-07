package com.stockheap.weather.service.weather;

import com.stockheap.weather.WeatherConstants;
import com.stockheap.weather.data.common.dto.WeatherData;
import com.stockheap.weather.service.external_weather.common.ExternalWeatherMethods;
import com.stockheap.weather.service.external_weather.dto.WeatherDataAndResponseStatusDTO;
import com.stockheap.weather.service.external_weather.open_weather.response_data.Sys;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.List;


@AllArgsConstructor
@Service
public class WeatherMethodsImpl implements WeatherMethods {

    @Autowired
    private ExternalWeatherMethods externalWeatherMethods;

    @Autowired
    private CacheManager cacheManager;

    private final String CACHE_ZIP_PREFIX_CURRENT = "CACHE_ZIP_PREFIX_CURRENT_";
    private final String CACHE_ZIP_PREFIX_EXT = "CACHE_ZIP_PREFIX_EXT_";


    public Mono<WeatherDataAndResponseStatusDTO> getCurrentWeather(String zip, String country) {


        System.out.println("@@@@ "+ externalWeatherMethods );

        WeatherData weatherData = (WeatherData) cacheManager.getCache(WeatherConstants.CacheNames.WEATHER_FORECAST_CACHE).get(CACHE_ZIP_PREFIX_CURRENT + zip);
        if (weatherData != null) {
            WeatherDataAndResponseStatusDTO weatherDataAndResponseStatusDTO = new WeatherDataAndResponseStatusDTO();
            weatherDataAndResponseStatusDTO.addWeatherData(weatherData);
            return Mono.just(weatherDataAndResponseStatusDTO);
        }
        return externalWeatherMethods.getCurrentWeather(zip, country);


    }

    public Mono<WeatherDataAndResponseStatusDTO> getExtendedWeather(String zip, String country) {
        List<WeatherData> weatherData = (List<WeatherData>) cacheManager.getCache(WeatherConstants.CacheNames.WEATHER_FORECAST_CACHE).get(CACHE_ZIP_PREFIX_EXT + zip);
        if (weatherData != null && weatherData.size() > 0) {
            WeatherDataAndResponseStatusDTO weatherDataAndResponseStatusDTO = new WeatherDataAndResponseStatusDTO();
            weatherDataAndResponseStatusDTO.addWeatherData(weatherData);
            return Mono.just(weatherDataAndResponseStatusDTO);
        }
        return externalWeatherMethods.getExtendedWeather(zip, country);
    }


}
