package com.stockheap.weather.service.external_weather.open_weather.response_data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class OpenExtendedWeatherResponse implements Serializable {

    private List<MainAndDateTime> list = new ArrayList<>();
    private int cod;
    private City city;


    private boolean isValid = true;
    private long statusCode;
    private String message ="";

    public OpenExtendedWeatherResponse() {
        super();

    }

    public OpenExtendedWeatherResponse(boolean isValid, long statusCode) {
        this.isValid = isValid;
        this.statusCode = statusCode;

    }

    public OpenExtendedWeatherResponse(boolean isValid, long statusCode, String message) {
        this.isValid = isValid;
        this.statusCode = statusCode;
        this.message = message;

    }

    @JsonSetter("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonIgnore
    public boolean isValid() {
        return isValid;
    }

    @JsonIgnore
    public long getStatusCode() {
        return statusCode;
    }


    @JsonSetter("list")
    public void setList(List<MainAndDateTime> list) {
        this.list = list;
    }

    @JsonSetter("cod")
    public void setCod(int cod) {
        this.cod = cod;
    }

    @JsonSetter("cod")
    public void setCod(String cod) {
        if(StringUtils.isNotBlank(cod))
        {
            this.cod = Integer.parseInt(cod);
        }


    }
    @JsonSetter("city")
    public void setCity(City city) {
        this.city = city;
    }
}
