package com.stockheap.weather.controller.response;

import com.stockheap.weather.data.common.dto.WeatherDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentWeatherResponse implements Serializable {

    private WeatherDataDTO current;
    private boolean cached;
    private String message;
    private String city;



}
