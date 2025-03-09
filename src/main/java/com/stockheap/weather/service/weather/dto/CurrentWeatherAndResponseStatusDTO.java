package com.stockheap.weather.service.weather.dto;

import com.stockheap.weather.data.common.dto.WeatherDataDTO;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CurrentWeatherAndResponseStatusDTO extends BaseWeatherAndResponseStatusDTO{


    private WeatherDataDTO current;

    public CurrentWeatherAndResponseStatusDTO()
    {
        super();
    }




    public CurrentWeatherAndResponseStatusDTO(Integer statusCode, boolean fromCache, String message)
    {
        super(statusCode);
        setFromCache(fromCache);
        setMessage(message);
    }



}
