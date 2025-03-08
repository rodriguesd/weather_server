package com.stockheap.weather.functional;


import com.stockheap.weather.WeatherApplication;
import com.stockheap.weather.service.weather.WeatherMethods;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes={WeatherApplication.class})

//@WebMvcTest(WeatherController.class) // Specify the controller to test
public class WeatherFunctionalTest {

    @Autowired
    private WeatherMethods weatherMethods;

//    @Autowired
//    private MockMvc mockMvc;


    @Test
    public void testCurrentWeather() throws Exception
    {
//        String zip = "94501";
//        String country = "us";
//        Mono<WeatherDataAndResponseStatusDTO> weatherDataAndResponseStatusDTOMono  =  weatherMethods.getExtendedWeather(zip, country);
//        weatherDataAndResponseStatusDTOMono.subscribe(s->{
//
//            String a1 = "";
//
//        });
//        Thread.sleep(2000l);
    }
}
