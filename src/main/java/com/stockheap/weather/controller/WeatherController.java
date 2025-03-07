package com.stockheap.weather.controller;

import com.stockheap.weather.controller.request.WeatherRequest;
import com.stockheap.weather.controller.response.WeatherResponse;
import com.stockheap.weather.data.common.dto.WeatherData;
import com.stockheap.weather.service.external_weather.dto.WeatherDataAndResponseStatusDTO;
import com.stockheap.weather.service.weather.WeatherMethods;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/weather")
@AllArgsConstructor
public class WeatherController {


    private WeatherMethods weatherMethods;


    @GetMapping("/v1/current/{countryCode}/{zip}")
    public Mono<ResponseEntity<WeatherResponse>> current(@PathVariable("countryCode") String countryCode, @PathVariable("zip") String zip) {
        return weatherMethods.getCurrentWeather(zip, countryCode)
                .map(weatherDataAndResponseStatusDTO -> {
                    if (weatherDataAndResponseStatusDTO != null && weatherDataAndResponseStatusDTO.ok()) {
                        WeatherResponse weatherResponse = new WeatherResponse(weatherDataAndResponseStatusDTO.getCurrent(), new ArrayList<>());
                        return ResponseEntity.ok(weatherResponse);
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new WeatherResponse());
                    }
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new WeatherResponse()));
    }

    @GetMapping("/v1/extended/{countryCode}/{zip}")
    public Mono<ResponseEntity<WeatherResponse>> extended(@PathVariable("countryCode") String countryCode, @PathVariable("zip") String zip) {
        return weatherMethods.getExtendedWeather(zip, countryCode)
                .map(weatherDataAndResponseStatusDTO -> {
                    if (weatherDataAndResponseStatusDTO != null && weatherDataAndResponseStatusDTO.ok()) {
                        WeatherResponse weatherResponse = new WeatherResponse(null, weatherDataAndResponseStatusDTO.getExtended());
                        return ResponseEntity.ok(weatherResponse);
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new WeatherResponse());
                    }
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new WeatherResponse()));
    }

//    @GetMapping("/v1/currentAndExtended/{countryCode}/{zip}")
//    public Mono<ResponseEntity<WeatherResponse>> currentAndExtended(@PathVariable("countryCode") String countryCode, @PathVariable("zip") String zip) {
//
//        Mono<WeatherDataAndResponseStatusDTO> ext = weatherMethods.getExtendedWeather(zip, countryCode);
//        Mono<WeatherDataAndResponseStatusDTO> current = weatherMethods.getCurrentWeather(zip, countryCode);
//        Mono<Tuple2<WeatherDataAndResponseStatusDTO, WeatherDataAndResponseStatusDTO>> combinedMono = Mono.zip(current, ext).
//                onErrorResume(error -> {
//            return Mono.just(Tuple2.of(new WeatherDataAndResponseStatusDTO() , new WeatherDataAndResponseStatusDTO()));
//        });;
//
//        return combinedMono.map(tuple -> {
//
//                    if (tuple.getT1().ok() && tuple.getT2().ok()) {
//                        WeatherResponse weatherResponse = new WeatherResponse(tuple.getT1().getCurrent(), tuple.getT2().getExtended());
//                        ResponseEntity.ok(weatherResponse);
//                    } else {
//                        ResponseEntity.status(HttpStatus.NOT_FOUND).body(new WeatherResponse());
//                    }
//                    return null;
//                }
//        );
//
//    }

}
