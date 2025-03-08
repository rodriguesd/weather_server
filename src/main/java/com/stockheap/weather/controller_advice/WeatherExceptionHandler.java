package com.stockheap.weather.controller_advice;

import com.stockheap.weather.controller_advice.response.GenericWeatherResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WeatherExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<GenericWeatherResponse> weatherServerException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new GenericWeatherResponse(exception.getMessage()));
    }
}
