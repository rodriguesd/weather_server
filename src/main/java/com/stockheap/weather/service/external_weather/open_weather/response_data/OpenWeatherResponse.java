package com.stockheap.weather.service.external_weather.open_weather.response_data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.springframework.http.HttpStatusCode;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherResponse {


    private long timezone;

    private long id;
    private String name;
    private long cod;
    private long dt;
    private Coord coord;

    private Main main;
    private List<Weather> weather;
    private Sys sys;

    private boolean isValid = true;
    private HttpStatusCode httpStatusCode = HttpStatusCode.valueOf(200);

    public OpenWeatherResponse() {
        super();

    }

    public OpenWeatherResponse(boolean isValid, HttpStatusCode httpStatusCode) {
        this.isValid = isValid;
        this.httpStatusCode = httpStatusCode;

    }

    @JsonIgnore
    public boolean isValid() {
        return isValid;
    }

    @JsonIgnore
    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }

    public long getDt() {
        return dt;
    }

    @JsonSetter("dt")
    public void setDt(long dt) {
        this.dt = dt;
    }

    public long getTimezone() {
        return timezone;
    }

    @JsonSetter("timezone")
    public void setTimezone(long timezone) {
        this.timezone = timezone;
    }

    public long getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    public long getCod() {
        return cod;
    }

    @JsonSetter("cod")
    public void setCod(long cod) {
        this.cod = cod;
    }

    public Coord getCoord() {
        return coord;
    }

    @JsonSetter("coord")
    public void setCoord(Coord coord) {
        this.coord = coord;
    }


    public Main getMain() {
        return main;
    }

    @JsonSetter("main")
    public void setMain(Main main) {
        this.main = main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    @JsonSetter("weather")
    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }


    public Sys getSys() {
        return sys;
    }

    @JsonSetter("sys")
    public void setSys(Sys sys) {
        this.sys = sys;
    }
}
