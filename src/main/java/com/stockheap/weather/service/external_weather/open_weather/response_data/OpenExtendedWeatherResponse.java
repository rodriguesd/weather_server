package com.stockheap.weather.service.external_weather.open_weather.response_data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.springframework.http.HttpStatusCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenExtendedWeatherResponse implements Serializable {

    private List<MainAndDateTime> list = new ArrayList<>();
    private long cod;
    private City city;


    private boolean isValid = true;
    private long statusCode;

    public OpenExtendedWeatherResponse() {
        super();

    }

    public OpenExtendedWeatherResponse(boolean isValid, long statusCode) {
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





    public List<MainAndDateTime> getList() {
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    @JsonSetter("list")
    public void setList(List<MainAndDateTime> list) {
        this.list = list;
    }

    public long getCod() {
        return cod;
    }

    @JsonSetter("cod")
    public void setCod(long cod) {
        this.cod = cod;
    }

    public City getCity() {
        return city;
    }

    @JsonSetter("city")
    public void setCity(City city) {
        this.city = city;
    }
}
