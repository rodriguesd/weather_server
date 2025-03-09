package com.stockheap.weather.service;


import com.stockheap.weather.helpers.ResourceFileReaderSingleton;
import com.stockheap.weather.helpers.TestDataUtils;
import com.stockheap.weather.service.common.ExternalWeatherMethods;
import com.stockheap.weather.service.weather.dto.CurrentWeatherAndResponseStatusDTO;
import com.stockheap.weather.service.external_weather.open_weather.OpenWeatherMethodsImpl;
import com.stockheap.weather.service.weather.WeatherMethods;
import com.stockheap.weather.service.weather.WeatherMethodsImpl;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;


import static org.mockito.Mockito.*;

@SpringBootTest
public class CurrentWeatherMethodsTest {



    @Test
    public void testExtendedOk200() throws Exception {


        String zip = "94121";
        String countryCode = "US";

        String ok200 = ResourceFileReaderSingleton.getInstance().readFile("open_weather_current_200.json");
        ExternalWeatherMethods externalWeatherMethods = Mockito.mock(OpenWeatherMethodsImpl.class);

        assertTrue(StringUtils.isNotBlank(ok200));

        Mono<CurrentWeatherAndResponseStatusDTO> mono =     TestDataUtils.createCurrent(ok200, zip, countryCode);
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

        Mono<CurrentWeatherAndResponseStatusDTO> mono =     TestDataUtils.createCurrent(error400, zip, countryCode);
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
        assertTrue(StringUtils.isNotBlank(currentWeatherAndResponseStatusDTO.getMessage()));
    }


    @Test
    public void testExtendedInvalidZip() throws Exception {
        String zip = "12345";
        String countryCode = "US";

        String error400 = ResourceFileReaderSingleton.getInstance().readFile("open_weather_current_400.json");
        ExternalWeatherMethods externalWeatherMethods = Mockito.mock(OpenWeatherMethodsImpl.class);

        assertTrue(StringUtils.isNotBlank(error400));

        Mono<CurrentWeatherAndResponseStatusDTO> mono =     TestDataUtils.createCurrent(error400, zip, countryCode);
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
        assertTrue(StringUtils.isNotBlank(currentWeatherAndResponseStatusDTO.getMessage()));
    }


//    Error 401 - Unauthorized. You can get 401 error if API token did not providen in
//    the request or in case API token provided in the request does not grant access to this API.
//    You must add API token with granted access to the product to the request before returning it.

    @Test
    public void testExtended401() throws Exception{


        String zip = "94121";
        String countryCode = "US";

        String error401 = ResourceFileReaderSingleton.getInstance().readFile("open_weather_current_401.json");
        ExternalWeatherMethods externalWeatherMethods = Mockito.mock(OpenWeatherMethodsImpl.class);

        assertTrue(StringUtils.isNotBlank(error401));

        Mono<CurrentWeatherAndResponseStatusDTO> mono =     TestDataUtils.createCurrent(error401, zip, countryCode);
        when(externalWeatherMethods.getCurrentWeather(zip,countryCode )).thenReturn(mono);

        WeatherMethods weatherMethods = new WeatherMethodsImpl(externalWeatherMethods);
        Mono<CurrentWeatherAndResponseStatusDTO> result = weatherMethods.getCurrentWeather(zip, countryCode);

        CurrentWeatherAndResponseStatusDTO currentWeatherAndResponseStatusDTO  = result.block();
        assertTrue(currentWeatherAndResponseStatusDTO != null);
        assertFalse(currentWeatherAndResponseStatusDTO.isFromCache());
        assertFalse(currentWeatherAndResponseStatusDTO.ok());
        assertTrue(currentWeatherAndResponseStatusDTO.getCurrent() == null);
        assertTrue(StringUtils.isNotBlank(currentWeatherAndResponseStatusDTO.getZip()));
        assertTrue(StringUtils.isNotBlank(currentWeatherAndResponseStatusDTO.getCountryCode()));
        assertTrue(currentWeatherAndResponseStatusDTO.getStatusCode() == 401);
        assertTrue(StringUtils.isNotBlank(currentWeatherAndResponseStatusDTO.getMessage()));
    }


   // Error 404 - Not Found. You can get 404 error if data with
    // requested parameters (lat, lon, date etc) does not exist in service database.
    // You must not retry the same request.

    @Test
    public void testExtended404() throws Exception {
        String zip = "94121";
        String countryCode = "US";

        String error401 = ResourceFileReaderSingleton.getInstance().readFile("open_weather_current_404.json");
        ExternalWeatherMethods externalWeatherMethods = Mockito.mock(OpenWeatherMethodsImpl.class);

        assertTrue(StringUtils.isNotBlank(error401));

        Mono<CurrentWeatherAndResponseStatusDTO> mono =     TestDataUtils.createCurrent(error401, zip, countryCode);
        when(externalWeatherMethods.getCurrentWeather(zip,countryCode )).thenReturn(mono);

        WeatherMethods weatherMethods = new WeatherMethodsImpl(externalWeatherMethods);
        Mono<CurrentWeatherAndResponseStatusDTO> result = weatherMethods.getCurrentWeather(zip, countryCode);

        CurrentWeatherAndResponseStatusDTO currentWeatherAndResponseStatusDTO  = result.block();
        assertTrue(currentWeatherAndResponseStatusDTO != null);
        assertFalse(currentWeatherAndResponseStatusDTO.isFromCache());
        assertFalse(currentWeatherAndResponseStatusDTO.ok());
        assertTrue(currentWeatherAndResponseStatusDTO.getCurrent() == null);
        assertTrue(StringUtils.isNotBlank(currentWeatherAndResponseStatusDTO.getZip()));
        assertTrue(StringUtils.isNotBlank(currentWeatherAndResponseStatusDTO.getCountryCode()));
        assertTrue(currentWeatherAndResponseStatusDTO.getStatusCode() == 404);
        assertTrue(StringUtils.isNotBlank(currentWeatherAndResponseStatusDTO.getMessage()));
    }



    //Error 429 - Too Many Requests. You can get 429 error if key quota of requests for provided API to this API was exceeded.
    //You may retry request after some time or after extending your key quota.
    @Test
    public void testExtended429() throws Exception{

        String zip = "94121";
        String countryCode = "US";

        String error401 = ResourceFileReaderSingleton.getInstance().readFile("open_weather_current_429.json");
        ExternalWeatherMethods externalWeatherMethods = Mockito.mock(OpenWeatherMethodsImpl.class);

        assertTrue(StringUtils.isNotBlank(error401));

        Mono<CurrentWeatherAndResponseStatusDTO> mono =     TestDataUtils.createCurrent(error401, zip, countryCode);
        when(externalWeatherMethods.getCurrentWeather(zip,countryCode )).thenReturn(mono);

        WeatherMethods weatherMethods = new WeatherMethodsImpl(externalWeatherMethods);
        Mono<CurrentWeatherAndResponseStatusDTO> result = weatherMethods.getCurrentWeather(zip, countryCode);

        CurrentWeatherAndResponseStatusDTO currentWeatherAndResponseStatusDTO  = result.block();
        assertTrue(currentWeatherAndResponseStatusDTO != null);
        assertFalse(currentWeatherAndResponseStatusDTO.isFromCache());
        assertFalse(currentWeatherAndResponseStatusDTO.ok());
        assertTrue(currentWeatherAndResponseStatusDTO.getCurrent() == null);
        assertTrue(StringUtils.isNotBlank(currentWeatherAndResponseStatusDTO.getZip()));
        assertTrue(StringUtils.isNotBlank(currentWeatherAndResponseStatusDTO.getCountryCode()));
        assertTrue(currentWeatherAndResponseStatusDTO.getStatusCode() == 429);
        assertTrue(StringUtils.isNotBlank(currentWeatherAndResponseStatusDTO.getMessage()));

    }

    //from open weather
    //Errors 5xx - Unexpected Error. You can get '5xx' error in case of other internal errors.
    //Error Response code will be `5xx`.
    //Please contact us and enclose an example of your API request that receives
    //this error into your email to let us analyze it and find a solution
    //for you promptly. You may retry the request which led to this error.
    @Test
    public void testExtended500() throws Exception{

        String zip = "94121";
        String countryCode = "US";

        String error401 = ResourceFileReaderSingleton.getInstance().readFile("open_weather_current_500.json");
        ExternalWeatherMethods externalWeatherMethods = Mockito.mock(OpenWeatherMethodsImpl.class);

        assertTrue(StringUtils.isNotBlank(error401));

        Mono<CurrentWeatherAndResponseStatusDTO> mono =     TestDataUtils.createCurrent(error401, zip, countryCode);
        when(externalWeatherMethods.getCurrentWeather(zip,countryCode )).thenReturn(mono);

        WeatherMethods weatherMethods = new WeatherMethodsImpl(externalWeatherMethods);
        Mono<CurrentWeatherAndResponseStatusDTO> result = weatherMethods.getCurrentWeather(zip, countryCode);

        CurrentWeatherAndResponseStatusDTO currentWeatherAndResponseStatusDTO  = result.block();
        assertTrue(currentWeatherAndResponseStatusDTO != null);
        assertFalse(currentWeatherAndResponseStatusDTO.isFromCache());
        assertFalse(currentWeatherAndResponseStatusDTO.ok());
        assertTrue(currentWeatherAndResponseStatusDTO.getCurrent() == null);
        assertTrue(StringUtils.isNotBlank(currentWeatherAndResponseStatusDTO.getZip()));
        assertTrue(StringUtils.isNotBlank(currentWeatherAndResponseStatusDTO.getCountryCode()));
        assertTrue(currentWeatherAndResponseStatusDTO.getStatusCode() == 500);
        assertTrue(StringUtils.isNotBlank(currentWeatherAndResponseStatusDTO.getMessage()));

    }



}
