package com.stockheap.weather.data.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class WeatherData implements Serializable {

    private String  date;
    private float currentTemp;
    private float highTemp;
    private float lowTemp;
    private String units;

}
