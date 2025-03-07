package com.stockheap.weather.service.external_weather.open_weather.response_data;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Sys {

    private int type;

    private int id;

    private String country;

    private long sunrise;

    private long sunset;


    public int getType() {
        return type;
    }

    @JsonSetter("type")
    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    @JsonSetter("country")
    public void setCountry(String country) {
        this.country = country;
    }

    public long getSunrise() {
        return sunrise;
    }

    @JsonSetter("sunrise")
    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    @JsonSetter("sunset")
    public void setSunset(long sunset) {
        this.sunset = sunset;
    }
}