package com.stockheap.weather.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockheap.weather.helpers.ResourceFileReaderSingleton;
import com.stockheap.weather.helpers.TestDataUtils;
import com.stockheap.weather.service.common.ExternalWeatherMethods;
import com.stockheap.weather.service.weather.dto.ExtendedWeatherAndResponseStatusDTO;
import com.stockheap.weather.service.external_weather.open_weather.OpenWeatherMethodsImpl;
import com.stockheap.weather.service.weather.WeatherMethods;
import com.stockheap.weather.service.weather.WeatherMethodsImpl;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ExtendedWeatherMethodsTest {


    private static ObjectMapper MAPPER = new ObjectMapper();

    @PostConstruct
    private void init() {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    @Test
    public void testExtendedOk200() throws Exception {


        String zip = "94121";
        String countryCode = "US";

        String ok200 = ResourceFileReaderSingleton.getInstance().readFile("open_weather_ex_200.json");
        ExternalWeatherMethods externalWeatherMethods = Mockito.mock(OpenWeatherMethodsImpl.class);

        assertTrue(StringUtils.isNotBlank(ok200));

        Mono<ExtendedWeatherAndResponseStatusDTO> mono = TestDataUtils.createCurrentExt(ok200, zip, countryCode);
        when(externalWeatherMethods.getExtendedWeather(zip,countryCode )).thenReturn(mono);

        WeatherMethods weatherMethods = new WeatherMethodsImpl(externalWeatherMethods);
        Mono<ExtendedWeatherAndResponseStatusDTO> result = weatherMethods.getExtendedWeather(zip, countryCode);

        ExtendedWeatherAndResponseStatusDTO currentWeatherAndResponseStatusDTO  = result.block();
        assertTrue(currentWeatherAndResponseStatusDTO != null);
        assertTrue(!currentWeatherAndResponseStatusDTO.isFromCache());
        assertTrue(currentWeatherAndResponseStatusDTO.ok());
        assertTrue(currentWeatherAndResponseStatusDTO.getExtended() != null);
        assertTrue(currentWeatherAndResponseStatusDTO.getExtended().size() > 0);
        assertTrue(StringUtils.isNotBlank(currentWeatherAndResponseStatusDTO.getZip()));
        assertTrue(StringUtils.isNotBlank(currentWeatherAndResponseStatusDTO.getCountryCode()));

    }








}
