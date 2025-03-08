package com.stockheap.weather.service.external_weather.dto;

import com.stockheap.weather.data.common.dto.WeatherData;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CurrentWeatherAndResponseStatusDTO extends BaseWeatherAndResponseStatusDTO{


    private WeatherData current;

    public CurrentWeatherAndResponseStatusDTO()
    {
        super();
    }

    public CurrentWeatherAndResponseStatusDTO(Integer statusCode)
    {
        super(statusCode);
    }


    public CurrentWeatherAndResponseStatusDTO(Integer statusCode, boolean fromCache)
    {
        super(statusCode);
        setFromCache(fromCache);
    }



}
