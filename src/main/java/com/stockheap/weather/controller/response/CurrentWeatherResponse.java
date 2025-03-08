package com.stockheap.weather.controller.response;

import com.stockheap.weather.data.common.dto.WeatherData;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class CurrentWeatherResponse implements Serializable {

    private WeatherData current;
    public CurrentWeatherResponse()
    {
        super();
    }
    public CurrentWeatherResponse(WeatherData current, List<WeatherData> extended)
    {
        this.current = current;
    }

}
