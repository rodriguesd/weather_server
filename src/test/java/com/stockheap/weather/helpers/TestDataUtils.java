package com.stockheap.weather.helpers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockheap.weather.service.weather.dto.CurrentWeatherAndResponseStatusDTO;
import com.stockheap.weather.service.weather.dto.ExtendedWeatherAndResponseStatusDTO;
import com.stockheap.weather.service.external_weather.open_weather.OpenWeatherMethodsImpl;
import com.stockheap.weather.service.external_weather.open_weather.response_data.OpenExtendedWeatherResponse;
import com.stockheap.weather.service.external_weather.open_weather.response_data.OpenWeatherResponse;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDataUtils {
    private TestDataUtils()
    {

    }

    private static ObjectMapper MAPPER = new ObjectMapper();


  static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    public static   Mono<ExtendedWeatherAndResponseStatusDTO> createCurrentExt(String data, String zip, String countryCode) throws Exception
    {
        OpenExtendedWeatherResponse openWeatherResponse = MAPPER.readValue(data, OpenExtendedWeatherResponse.class);
        assertTrue(openWeatherResponse != null);
        OpenWeatherMethodsImpl weatherMethods  = new OpenWeatherMethodsImpl();
        ExtendedWeatherAndResponseStatusDTO currentWeatherAndResponseStatusDTO =  weatherMethods.createWeatherDataAndResponseStatusDTO(zip,
                countryCode,
                "imperial",
                openWeatherResponse);
        return Mono.just(currentWeatherAndResponseStatusDTO);
    }



    public static Mono<CurrentWeatherAndResponseStatusDTO> createCurrent(String data, String zip, String countryCode) throws Exception
    {
        OpenWeatherResponse openWeatherResponse = MAPPER.readValue(data, OpenWeatherResponse.class);
        assertTrue(openWeatherResponse != null);
        OpenWeatherMethodsImpl weatherMethods  = new OpenWeatherMethodsImpl();
        CurrentWeatherAndResponseStatusDTO currentWeatherAndResponseStatusDTO =  weatherMethods.createWeatherDataAndResponseStatusDTO(zip,
                countryCode,
                "imperial",
                openWeatherResponse);
        return Mono.just(currentWeatherAndResponseStatusDTO);
    }
}
