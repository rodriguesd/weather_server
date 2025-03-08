package com.stockheap.weather.service.external_weather.open_weather.response_data;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class Coord implements Serializable {

    private int id;
    private String main;

    private String description;
    private String icon;

    @JsonSetter("id")
    public void setId(int id) {
        this.id = id;
    }


    @JsonSetter("main")
    public void setMain(String main) {
        this.main = main;
    }


    @JsonSetter("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonSetter("icon")
    public void setIcon(String icon) {
        this.icon = icon;
    }
}
