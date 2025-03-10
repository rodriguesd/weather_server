package com.stockheap.weather.service.common.dto;

import com.stockheap.weather.data.common.dto.WeatherDataDTO;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;



/**
 * <p>
 *     This dto is used to transfer extended weather data.
 * </p>
 */


@Getter
@Setter
public class ExtendedWeatherAndResponseStatusDTO extends BaseWeatherAndResponseStatusDTO {

    private List<WeatherDataDTO> extended = new ArrayList<>();

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

    public void addWeatherData(WeatherDataDTO data) {
        if (data != null) {
            if (extended == null) {
                extended = new ArrayList<>();
            }
            extended.add(data);
        }
    }

    public void addWeatherData(List<WeatherDataDTO> data) {
        if (data != null &&
                data.size() > 0) {
            if (extended == null) {
                extended = new ArrayList<>();
            }
            extended.addAll(data);
        }
    }

}
