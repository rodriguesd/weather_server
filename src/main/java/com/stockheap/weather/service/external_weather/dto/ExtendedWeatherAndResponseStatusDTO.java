package com.stockheap.weather.service.external_weather.dto;

import com.stockheap.weather.data.common.dto.WeatherData;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ExtendedWeatherAndResponseStatusDTO extends BaseWeatherAndResponseStatusDTO {

    private List<WeatherData> extended = new ArrayList<>();

    public ExtendedWeatherAndResponseStatusDTO()
    {
        super();
    }
    public ExtendedWeatherAndResponseStatusDTO(Integer statusCode)
    {
        super(statusCode);
    }

    public ExtendedWeatherAndResponseStatusDTO(Integer statusCode, boolean fromCache, String message)
    {
        super(statusCode);
        setFromCache(fromCache);
        setMessage(message);
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
