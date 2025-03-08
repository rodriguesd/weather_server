package com.stockheap.weather.service.external_weather.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BaseWeatherAndResponseStatusDTO implements Serializable {


    private String zip;
    private String countryCode;
    private Integer statusCode = 0;
    private boolean fromCache = true;
    private String message;

    public BaseWeatherAndResponseStatusDTO()
    {
        super();
    }

    public BaseWeatherAndResponseStatusDTO(Integer statusCode)
    {

        this.statusCode = statusCode;
    }


    public boolean ok() {
        return statusCode == 200;
    }

    public boolean isFromCache()
    {
        return fromCache;
    }






}
