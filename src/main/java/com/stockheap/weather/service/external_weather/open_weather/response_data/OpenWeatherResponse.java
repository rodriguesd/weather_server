package com.stockheap.weather.service.external_weather.open_weather.response_data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class OpenWeatherResponse {


    private long timezone;

    private long id;
    private String name;
    private int cod;
    private long dt;
    private Coord coord;

    private Main main;
    private List<Weather> weather;
    private Sys sys;

    private boolean isValid = true;
    private long statusCode = HttpStatus.OK.value();
    private String message ="";

    public OpenWeatherResponse() {
        super();

    }

    public OpenWeatherResponse(boolean isValid, long statusCode) {
        this.isValid = isValid;
        this.statusCode = statusCode;

    }

    @JsonIgnore
    public boolean isValid() {
        return isValid;
    }

    @JsonIgnore
    public long getStatusCode() {
        return statusCode;
    }

    @JsonSetter("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonSetter("dt")
    public void setDt(long dt) {
        this.dt = dt;
    }

    @JsonSetter("timezone")
    public void setTimezone(long timezone) {
        this.timezone = timezone;
    }


    @JsonSetter("id")
    public void setId(long id) {
        this.id = id;
    }


    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }


    @JsonSetter("cod")
    public void setCod(int cod) {
        this.cod = cod;
    }


    @JsonSetter("coord")
    public void setCoord(Coord coord) {
        this.coord = coord;
    }


    @JsonSetter("main")
    public void setMain(Main main) {
        this.main = main;
    }


    @JsonSetter("weather")
    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }


    @JsonSetter("sys")
    public void setSys(Sys sys) {
        this.sys = sys;
    }
}
