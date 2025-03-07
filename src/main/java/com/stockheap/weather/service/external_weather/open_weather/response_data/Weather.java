package com.stockheap.weather.service.external_weather.open_weather.response_data;

import com.fasterxml.jackson.annotation.JsonSetter;

public class Weather {

    private int id;

    private String main;

    private String description;

    private String icon;





    public int getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(int id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    @JsonSetter("main")
    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    @JsonSetter("description")
    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }
    @JsonSetter("icon")
    public void setIcon(String icon) {
        this.icon = icon;
    }
}