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
    private HttpStatusCode httpStatusCode = HttpStatusCode.valueOf(200);

    public OpenExtendedWeatherResponse() {
        super();

    }

    public OpenExtendedWeatherResponse(boolean isValid, HttpStatusCode httpStatusCode) {
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
