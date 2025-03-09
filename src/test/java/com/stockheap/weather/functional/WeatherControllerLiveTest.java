package com.stockheap.weather.functional;
import com.stockheap.weather.WeatherConstants;
import com.stockheap.weather.controller.response.CurrentWeatherResponse;
import com.stockheap.weather.controller.response.ExtendedWeatherResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherControllerLiveTest {


    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void flushCache()
    {

        Cache cache = cacheManager.getCache(WeatherConstants.CacheNames.WEATHER_FORECAST_CACHE);
        if (cache != null) {
            cache.clear();
        }
    }


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
