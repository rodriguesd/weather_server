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
    private long statusCode = 0;
    private boolean fromCache = true;

    public WeatherDataAndResponseStatusDTO()
    {
        super();
    }

    public WeatherDataAndResponseStatusDTO( long statusCode)
    {

        this.statusCode = statusCode;
    }

    public void setFromCache(boolean fromCache) {
        this.fromCache = fromCache;
    }

    public boolean ok() {
        return statusCode == 200;
    }

    public void setStatusCode(long statusCode) {
        this.statusCode = statusCode;
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

    public List<WeatherData> getExtended() {
        return extended;
    }

    public void setExtended(List<WeatherData> extended) {
        this.extended = extended;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }


    public WeatherData getCurrent() {
        return current;
    }

    public void setCurrent(WeatherData current) {
        this.current = current;
    }

    public Long getStatusCode() {
        return statusCode;
    }
}
