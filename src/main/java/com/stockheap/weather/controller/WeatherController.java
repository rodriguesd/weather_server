package com.stockheap.weather.controller;


import com.stockheap.weather.controller.response.CurrentWeatherResponse;
import com.stockheap.weather.controller.response.ExtendedWeatherResponse;
import com.stockheap.weather.service.weather.WeatherMethods;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Stack;


@RestController
@RequestMapping("/weather")
public class WeatherController {



    private final WeatherMethods weatherMethods;

    @Autowired
    public WeatherController(WeatherMethods weatherMethods) {
        this.weatherMethods = weatherMethods;
    }




    @GetMapping("/v1/current/{countryCode}/{zip}")
    public Mono<ResponseEntity<CurrentWeatherResponse>> current(@PathVariable("countryCode")  @NotBlank  String countryCode, @PathVariable("zip")  @NotBlank String zip) {
        return weatherMethods.getCurrentWeather(zip, countryCode)
                .map(weatherDataAndResponseStatusDTO -> {
                    if (weatherDataAndResponseStatusDTO != null && weatherDataAndResponseStatusDTO.ok()) {
                        CurrentWeatherResponse currentWeatherResponse = new CurrentWeatherResponse(weatherDataAndResponseStatusDTO.getCurrent(),
                                                weatherDataAndResponseStatusDTO.isFromCache(), weatherDataAndResponseStatusDTO.getMessage());
                        return ResponseEntity.ok(currentWeatherResponse);
                    } else {
                        if(weatherDataAndResponseStatusDTO != null)
                        {
                            return ResponseEntity.status(HttpStatus.resolve(weatherDataAndResponseStatusDTO.getStatusCode().intValue())).body(new CurrentWeatherResponse(weatherDataAndResponseStatusDTO.getCurrent(),
                                   false, weatherDataAndResponseStatusDTO.getMessage()));
                        }
                        return ResponseEntity.status(HttpStatus.resolve(weatherDataAndResponseStatusDTO.getStatusCode().intValue())).body(new CurrentWeatherResponse());
                    }
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CurrentWeatherResponse()));
    }

    @GetMapping("/v1/extended/{countryCode}/{zip}")
    public Mono<ResponseEntity<ExtendedWeatherResponse>> extended(@PathVariable("countryCode") @NotBlank  String countryCode, @PathVariable("zip")  @NotBlank String zip) {
        return weatherMethods.getExtendedWeather(zip, countryCode)
                .map(weatherDataAndResponseStatusDTO -> {
                    if (weatherDataAndResponseStatusDTO != null && weatherDataAndResponseStatusDTO.ok()) {
                        ExtendedWeatherResponse extendedWeatherResponse = new ExtendedWeatherResponse(weatherDataAndResponseStatusDTO.getExtended(), weatherDataAndResponseStatusDTO.isFromCache(), weatherDataAndResponseStatusDTO.getMessage());
                        return ResponseEntity.ok(extendedWeatherResponse);
                    } else {
                        if(weatherDataAndResponseStatusDTO != null)
                        {
                            ExtendedWeatherResponse extendedWeatherResponse = new ExtendedWeatherResponse(weatherDataAndResponseStatusDTO.getExtended(), false, weatherDataAndResponseStatusDTO.getMessage());
                            return ResponseEntity.ok(extendedWeatherResponse);
                        }
                        return ResponseEntity.status(HttpStatus.resolve(weatherDataAndResponseStatusDTO.getStatusCode().intValue())).body(new ExtendedWeatherResponse());
                    }
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExtendedWeatherResponse()));
    }


}
