package com.stockheap.weather.service.external_weather.open_weather.response_data;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;

@Getter
public class MainAndDateTime {
    private Main main;
    private long dt;


    @JsonSetter("main")
    public void setMain(Main main) {
        this.main = main;
    }


    @JsonSetter("dt")
    public void setDt(long dt) {
        this.dt = dt;
    }
}
