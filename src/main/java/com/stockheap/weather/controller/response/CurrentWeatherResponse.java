package com.stockheap.weather.controller.response;

import com.stockheap.weather.data.common.dto.WeatherData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentWeatherResponse implements Serializable {

    private WeatherData current;
    private boolean cached;
    private String message;



}
