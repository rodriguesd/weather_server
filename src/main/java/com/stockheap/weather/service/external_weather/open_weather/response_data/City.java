package com.stockheap.weather.service.external_weather.open_weather.response_data;

import lombok.Getter;


import com.fasterxml.jackson.annotation.JsonSetter;

@Getter
public class City {
    private int id;
    private String name;
    private Coord coord;
    private String country;
    private int population;
    private int timezone;
    private long sunrise;
    private long sunset;




    @JsonSetter("id")
    public void setId(int id) {
        this.id = id;
    }


    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }


    @JsonSetter("coord")
    public void setCoord(Coord coord) {
        this.coord = coord;
    }



    @JsonSetter("country")
    public void setCountry(String country) {
        this.country = country;
    }



    @JsonSetter("population")
    public void setPopulation(int population) {
        this.population = population;
    }


    @JsonSetter("timezone")
    public void setTimezone(int timezone) {
        this.timezone = timezone;
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