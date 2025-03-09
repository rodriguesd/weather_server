package com.stockheap.weather.functional;
import com.stockheap.weather.controller.rest.response.CurrentWeatherResponse;
import com.stockheap.weather.controller.rest.response.ExtendedWeatherResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherControllerLiveTest {


    @Autowired
    private TestRestTemplate restTemplate;



    @Test
    public void testWeatherCurrent()
    {
        ResponseEntity<CurrentWeatherResponse> response = restTemplate.getForEntity("/weather/v1/current/US/94121", CurrentWeatherResponse.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        CurrentWeatherResponse weatherResponse =   response.getBody();
        assertTrue(weatherResponse != null);
        assertTrue(weatherResponse.getCurrent() != null);
        assertTrue(weatherResponse.getCurrent().getCurrentTemp() > 0);


    }


    @Test
    public void testWeatherExtended()
    {
        ResponseEntity<ExtendedWeatherResponse> response = restTemplate.getForEntity("/weather/v1/extended/US/94121", ExtendedWeatherResponse.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        ExtendedWeatherResponse weatherResponse =   response.getBody();
        assertTrue(weatherResponse != null);
        assertTrue(weatherResponse.getExtended() != null);
        assertTrue(weatherResponse.getExtended().size() > 0);
        assertTrue(weatherResponse.getExtended().get(0).getCurrentTemp() > 0);

    }
}
