package com.stockheap.weather.controller.response;

import com.stockheap.weather.data.common.dto.WeatherDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtendedWeatherResponse implements Serializable {

    private List<WeatherDataDTO> extended = new ArrayList<>();
    private boolean cached;
    private String message;


}
