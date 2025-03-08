package com.stockheap.weather.service.external_weather.open_weather.response_data;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;

@Getter
public class Sys {

    private int type;

    private int id;

    private String country;

    private long sunrise;

    private long sunset;




    @JsonSetter("type")
    public void setType(int type) {
        this.type = type;
    }


    @JsonSetter("id")
    public void setId(int id) {
        this.id = id;
    }


    @JsonSetter("country")
    public void setCountry(String country) {
        this.country = country;
    }



    @JsonSetter("sunrise")
    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    @JsonSetter("sunset")
    public void setSunset(long sunset) {
        this.sunset = sunset;
    }
}