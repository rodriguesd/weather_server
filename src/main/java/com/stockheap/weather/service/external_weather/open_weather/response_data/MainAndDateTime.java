package com.stockheap.weather.service.external_weather.open_weather.response_data;

import com.fasterxml.jackson.annotation.JsonSetter;

public class MainAndDateTime {
    private Main main;
    private long dt;

    public Main getMain() {
        return main;
    }

    @JsonSetter("main")
    public void setMain(Main main) {
        this.main = main;
    }

    public long getDt() {
        return dt;
    }
    @JsonSetter("dt")
    public void setDt(long dt) {
        this.dt = dt;
    }
}
