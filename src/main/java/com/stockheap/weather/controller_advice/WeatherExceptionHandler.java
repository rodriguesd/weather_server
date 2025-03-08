package com.stockheap.weather.controller_advice;

import com.stockheap.weather.controller_advice.response.GenericWeatherResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WeatherExceptionHandler {



    @ExceptionHandler(Exception.class)
    public String handleNotFound(Exception e, Model model) {
        model.addAttribute("message", "The REST API you are looking for doesn't exist.");
        return "error";
    }
}
