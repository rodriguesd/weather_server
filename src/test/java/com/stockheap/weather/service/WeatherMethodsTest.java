package com.stockheap.weather.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockheap.weather.data.common.dto.WeatherData;
import com.stockheap.weather.helpers.ResourceFileReaderSingleton;
import com.stockheap.weather.service.external_weather.common.ExternalWeatherMethods;
import com.stockheap.weather.service.external_weather.dto.CurrentWeatherAndResponseStatusDTO;
import com.stockheap.weather.service.external_weather.open_weather.OpenWeatherMethodsImpl;
import com.stockheap.weather.service.external_weather.open_weather.response_data.OpenWeatherResponse;
import com.stockheap.weather.service.weather.WeatherMethods;
import com.stockheap.weather.service.weather.WeatherMethodsImpl;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
public class WeatherMethodsTest {


    private static ObjectMapper MAPPER = new ObjectMapper();

    @PostConstruct
    private void init() {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    @Test
    public void testExtendedOk200() throws Exception {


        String zip = "94121";
        String countryCode = "US";

        String ok200 = ResourceFileReaderSingleton.getInstance().readFile("open_weather_current_200.json");
        ExternalWeatherMethods externalWeatherMethods = Mockito.mock(OpenWeatherMethodsImpl.class);

        assertTrue(StringUtils.isNotBlank(ok200));

        Mono<CurrentWeatherAndResponseStatusDTO> mono =     createCurrent(ok200, zip, countryCode);
        when(externalWeatherMethods.getCurrentWeather(zip,countryCode )).thenReturn(mono);

        WeatherMethods weatherMethods = new WeatherMethodsImpl(externalWeatherMethods);
        Mono<CurrentWeatherAndResponseStatusDTO> result = weatherMethods.getCurrentWeather(zip, countryCode);

        CurrentWeatherAndResponseStatusDTO currentWeatherAndResponseStatusDTO  = result.block();
        assertTrue(currentWeatherAndResponseStatusDTO != null);
        assertTrue(!currentWeatherAndResponseStatusDTO.isFromCache());
        assertTrue(currentWeatherAndResponseStatusDTO.ok());
        assertTrue(currentWeatherAndResponseStatusDTO.getCurrent() != null);
        assertTrue(StringUtils.isNotBlank(currentWeatherAndResponseStatusDTO.getZip()));
        assertTrue(StringUtils.isNotBlank(currentWeatherAndResponseStatusDTO.getCountryCode()));

    }





    //from open weather
    //Error 400 - Bad Request. You can get 400 error if either some mandatory parameters in
    //the request are missing or some of request parameters have incorrect format or
    //values out of allowed range. List of all parameters names that are missing or
    //incorrect will be returned in `parameters`attribute of the `ErrorResponse` object
    @Test
    public void testExtended400() throws Exception {
        String zip = "";
        String countryCode = "";

        String error400 = ResourceFileReaderSingleton.getInstance().readFile("open_weather_current_400.json");
        ExternalWeatherMethods externalWeatherMethods = Mockito.mock(OpenWeatherMethodsImpl.class);

        assertTrue(StringUtils.isNotBlank(error400));

        Mono<CurrentWeatherAndResponseStatusDTO> mono =     createCurrent(error400, zip, countryCode);
        when(externalWeatherMethods.getCurrentWeather(zip,countryCode )).thenReturn(mono);

        WeatherMethods weatherMethods = new WeatherMethodsImpl(externalWeatherMethods);
        Mono<CurrentWeatherAndResponseStatusDTO> result = weatherMethods.getCurrentWeather(zip, countryCode);

        CurrentWeatherAndResponseStatusDTO currentWeatherAndResponseStatusDTO  = result.block();
        assertTrue(currentWeatherAndResponseStatusDTO != null);
        assertFalse(currentWeatherAndResponseStatusDTO.isFromCache());
        assertFalse(currentWeatherAndResponseStatusDTO.ok());
        assertTrue(currentWeatherAndResponseStatusDTO.getStatusCode() == HttpStatus.BAD_REQUEST.value());
        assertTrue(currentWeatherAndResponseStatusDTO.getCurrent() == null);
        assertTrue(StringUtils.isBlank(currentWeatherAndResponseStatusDTO.getZip()));
        assertTrue(StringUtils.isBlank(currentWeatherAndResponseStatusDTO.getCountryCode()));
    }


    @Test
    public void testExtendedInvalidZip() throws Exception {
        String zip = "12345";
        String countryCode = "US";

        String error400 = ResourceFileReaderSingleton.getInstance().readFile("open_weather_current_400.json");
        ExternalWeatherMethods externalWeatherMethods = Mockito.mock(OpenWeatherMethodsImpl.class);

        assertTrue(StringUtils.isNotBlank(error400));

        Mono<CurrentWeatherAndResponseStatusDTO> mono =     createCurrent(error400, zip, countryCode);
        when(externalWeatherMethods.getCurrentWeather(zip,countryCode )).thenReturn(mono);

        WeatherMethods weatherMethods = new WeatherMethodsImpl(externalWeatherMethods);
        Mono<CurrentWeatherAndResponseStatusDTO> result = weatherMethods.getCurrentWeather(zip, countryCode);

        CurrentWeatherAndResponseStatusDTO currentWeatherAndResponseStatusDTO  = result.block();
        assertTrue(currentWeatherAndResponseStatusDTO != null);
        assertFalse(currentWeatherAndResponseStatusDTO.isFromCache());
        assertFalse(currentWeatherAndResponseStatusDTO.ok());
        assertTrue(currentWeatherAndResponseStatusDTO.getStatusCode() == HttpStatus.BAD_REQUEST.value());
        assertTrue(currentWeatherAndResponseStatusDTO.getCurrent() == null);
        assertTrue(StringUtils.isNotBlank(currentWeatherAndResponseStatusDTO.getZip()));
        assertTrue(StringUtils.isNotBlank(currentWeatherAndResponseStatusDTO.getCountryCode()));
    }


//    Error 401 - Unauthorized. You can get 401 error if API token did not providen in
//    the request or in case API token provided in the request does not grant access to this API.
//    You must add API token with granted access to the product to the request before returning it.

    @Test
    public void testExtended401() {

    }

    @Test
    public void testExtended404() {

    }

    @Test
    public void testExtended429() {

    }

    //from open weather
    //Errors 5xx - Unexpected Error. You can get '5xx' error in case of other internal errors.
    //Error Response code will be `5xx`.
    //Please contact us and enclose an example of your API request that receives
    //this error into your email to let us analyze it and find a solution
    //for you promptly. You may retry the request which led to this error.
    @Test
    public void testExtended500() {

    }

    private  Mono<CurrentWeatherAndResponseStatusDTO> createCurrent(String data, String zip, String countryCode) throws Exception
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
