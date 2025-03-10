package com.stockheap.weather.service.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;



/**
 * <p>
 *     This dto is used to transfer data from an external weather
 *     service to the caller. This class should be kept generic.
 *
 * </p>
 */


@Getter
@Setter
public class BaseWeatherAndResponseStatusDTO implements Serializable {


    private String zip;
    private String countryCode;
    private Integer statusCode = 0;
    @JsonIgnore
    private transient boolean fromCache = true;
    private String message;
    private String city;

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







}
