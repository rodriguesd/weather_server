package com.stockheap.weather.service.external_weather.dto;

import com.stockheap.weather.data.common.dto.WeatherData;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.io.Serializable;
import java.util.*;

@Data
public class WeatherDataAndResponseStatusDTO implements Serializable {

    private List<WeatherData> extended = new ArrayList<>();
    private String zip;
    private String countryCode;
    private HttpStatusCode httpStatusCode;
    private WeatherData current;

    public boolean ok()
    {
        return httpStatusCode != null && httpStatusCode.is2xxSuccessful();
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

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(HttpStatusCode httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public WeatherData getCurrent() {
        return current;
    }

    public void setCurrent(WeatherData current) {
        this.current = current;
    }
}
