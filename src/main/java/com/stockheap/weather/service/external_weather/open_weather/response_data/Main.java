package com.stockheap.weather.service.external_weather.open_weather.response_data;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;

@Getter
public class Main {
    private float temp;

    private float feelsLike;

    private float tempMin;

    private float tempMax;

    private int pressure;

    private int humidity;

    private int seaLevel;

    private int grndLevel;


    @JsonSetter("temp")
    public void setTemp(float temp) {
        this.temp = temp;
    }



    @JsonSetter("feels_like")
    public void setFeelsLike(float feelsLike) {
        this.feelsLike = feelsLike;
    }



    @JsonSetter("temp_min")
    public void setTempMin(float tempMin) {
        this.tempMin = tempMin;
    }


    @JsonSetter("temp_max")
    public void setTempMax(float tempMax) {
        this.tempMax = tempMax;
    }


    @JsonSetter("pressure")
    public void setPressure(int pressure) {
        this.pressure = pressure;
    }


    @JsonSetter("humidity")
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }



    @JsonSetter("sea_level")
    public void setSeaLevel(int seaLevel) {
        this.seaLevel = seaLevel;
    }


    @JsonSetter("grnd_level")
    public void setGrndLevel(int grndLevel) {
        this.grndLevel = grndLevel;
    }
}