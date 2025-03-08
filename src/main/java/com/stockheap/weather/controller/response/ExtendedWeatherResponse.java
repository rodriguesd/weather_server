package com.stockheap.weather.controller.response;

import com.stockheap.weather.data.common.dto.WeatherData;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
public class ExtendedWeatherResponse implements Serializable {

    private List<WeatherData> extended = new ArrayList<>();
    public ExtendedWeatherResponse()
    {
        super();
    }
    public ExtendedWeatherResponse(List<WeatherData> extended)
    {
        this.extended  = extended;
    }

}
