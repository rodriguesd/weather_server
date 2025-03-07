package com.stockheap.weather.controller.response;

import com.stockheap.weather.data.common.dto.WeatherData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
public class WeatherResponse implements Serializable {

    private WeatherData current;
    private List<WeatherData> extended = new ArrayList<>();
    public WeatherResponse()
    {
        super();
    }
    public WeatherResponse( WeatherData current, List<WeatherData> extended)
    {
        this.current = current;
        this.extended  = extended;
    }

}
