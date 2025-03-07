package com.stockheap.weather.service.external_weather.open_weather.response_data;

import com.fasterxml.jackson.annotation.JsonSetter;

public class Main {
    private float temp;

    private float feelsLike;

    private float tempMin;

    private float tempMax;

    private int pressure;

    private int humidity;

    private int seaLevel;

    private int grndLevel;

    public float getTemp() {
        return temp;
    }

    @JsonSetter("temp")
    public void setTemp(float temp) {
        this.temp = temp;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    @JsonSetter("feels_like")
    public void setFeelsLike(float feelsLike) {
        this.feelsLike = feelsLike;
    }

    public float getTempMin() {
        return tempMin;
    }

    @JsonSetter("temp_min")
    public void setTempMin(float tempMin) {
        this.tempMin = tempMin;
    }

    public float getTempMax() {
        return tempMax;
    }

    @JsonSetter("temp_max")
    public void setTempMax(float tempMax) {
        this.tempMax = tempMax;
    }

    public int getPressure() {
        return pressure;
    }

    @JsonSetter("pressure")
    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    @JsonSetter("humidity")
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getSeaLevel() {
        return seaLevel;
    }

    @JsonSetter("sea_level")
    public void setSeaLevel(int seaLevel) {
        this.seaLevel = seaLevel;
    }

    public int getGrndLevel() {
        return grndLevel;
    }

    @JsonSetter("grnd_level")
    public void setGrndLevel(int grndLevel) {
        this.grndLevel = grndLevel;
    }
}