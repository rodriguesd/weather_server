package com.stockheap.weather.functional;

import com.stockheap.weather.WeatherApplication;
import com.stockheap.weather.service.external_weather.dto.WeatherDataAndResponseStatusDTO;
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
        String zip = "94121";
        String country = "us";
        Mono<WeatherDataAndResponseStatusDTO> weatherDataAndResponseStatusDTOMono  =  weatherMethods.getCurrentWeather(zip, country);
        weatherDataAndResponseStatusDTOMono.subscribe(s->{


            String a1 = "";

        });
        Thread.sleep(2000l);
    }
}
