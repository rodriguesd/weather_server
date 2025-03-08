package com.stockheap.weather.service.external_weather.dto;

import com.stockheap.weather.data.common.dto.WeatherData;
import lombok.Data;


import java.io.Serializable;
import java.util.*;

@Data
public class WeatherDataAndResponseStatusDTO implements Serializable {

    private List<WeatherData> extended = new ArrayList<>();
    private String zip;
    private String countryCode;
    private WeatherData current;
    private Integer statusCode = 0;
    private boolean fromCache = true;

    public WeatherDataAndResponseStatusDTO()
    {
        super();
    }

    public WeatherDataAndResponseStatusDTO( Integer statusCode)
    {

        this.statusCode = statusCode;
    }


    public boolean ok() {
        return statusCode == 200;
    }


    public void addWeatherData(WeatherData data) {
        if (data != null) {
            if (extended == null) {
                extended = new ArrayList<>();
            }
            extended.add(data);
        }
    }

    public void addWeatherData(List<WeatherData> data) {
        if (data != null &&
                data.size() > 0) {
            if (extended == null) {
                extended = new ArrayList<>();
            }
            extended.addAll(data);
        }
    }

}
