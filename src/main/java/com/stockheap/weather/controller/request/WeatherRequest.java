package com.stockheap.weather.controller.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.io.Serializable;


@Getter
@Setter
public class WeatherRequest implements Serializable {


    private String country;
    private String zip;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
