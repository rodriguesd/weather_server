package com.stockheap.weather.service.common.dto;

import com.stockheap.weather.data.common.dto.WeatherDataDTO;
import lombok.Getter;
import lombok.Setter;



/**
 * <p>
 *     This dto is used to transfer current weather data.
 *
 * </p>
 */


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
