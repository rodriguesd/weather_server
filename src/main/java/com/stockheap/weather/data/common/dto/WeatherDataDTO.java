package com.stockheap.weather.data.common.dto;

import lombok.Data;

import java.io.Serializable;


/**
 * <p>
 *     This dto is used to represent the weather during a single day and is a generic
 *     class that is used in various parts of the application. Please keep it generic.
 * </p>
 */



@Data
public class WeatherDataDTO implements Serializable {

    private String  date;
    private float currentTemp;
    private float highTemp;
    private float lowTemp;
    private String units;

}
