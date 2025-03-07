package com.stockheap.weather.functional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockheap.weather.WeatherApplication;
import com.stockheap.weather.service.external_weather.dto.WeatherDataAndResponseStatusDTO;
import com.stockheap.weather.service.external_weather.open_weather.response_data.OpenExtendedWeatherResponse;
import com.stockheap.weather.service.external_weather.open_weather.response_data.Sys;
import com.stockheap.weather.service.weather.WeatherMethods;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

@SpringBootTest(classes={WeatherApplication.class})
public class WeatherFunctionalTest {

    @Autowired
    private WeatherMethods weatherMethods;

    @Test
    public void testCurrentWeather() throws Exception
    {
        String zip = "94501";
        String country = "us";
        Mono<WeatherDataAndResponseStatusDTO> weatherDataAndResponseStatusDTOMono  =  weatherMethods.getExtendedWeather(zip, country);
        weatherDataAndResponseStatusDTOMono.subscribe(s->{

            String a1 = "";

        });
        Thread.sleep(2000l);
    }
}
