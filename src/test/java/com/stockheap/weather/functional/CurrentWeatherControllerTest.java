package com.stockheap.weather.functional;

import com.stockheap.weather.controller.rest.WeatherController;

import com.stockheap.weather.controller.rest.response.CurrentWeatherResponse;
import com.stockheap.weather.helpers.ResourceFileReaderSingleton;
import com.stockheap.weather.helpers.TestDataUtils;
import com.stockheap.weather.service.common.ExternalWeatherMethods;
import com.stockheap.weather.service.weather.dto.CurrentWeatherAndResponseStatusDTO;
import com.stockheap.weather.service.weather.WeatherMethods;
import com.stockheap.weather.service.weather.WeatherMethodsImpl;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(WeatherController.class)
@Import({WeatherMethodsImpl.class})
class CurrentWeatherControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExternalWeatherMethods externalWeatherMethods;

    @Autowired
    private WeatherMethods weatherMethods;


    private Mono<CurrentWeatherAndResponseStatusDTO> generateMono(String fileName, String zip,
                                                                  String countryCode) throws Exception {


        String ok200 = ResourceFileReaderSingleton.getInstance().readFile(fileName);
        assertTrue(StringUtils.isNotBlank(ok200));
        return TestDataUtils.createCurrent(ok200, zip, countryCode);


    }


    @Test
    public void testCorrectData200() throws Exception {

        String zip = "94121";
        String countryCode = "US";
        Mono<CurrentWeatherAndResponseStatusDTO> mono = generateMono("open_weather_current_200.json", zip, countryCode);

        when(externalWeatherMethods.getCurrentWeather(zip, countryCode)).thenReturn(mono);
        weatherMethods = new WeatherMethodsImpl(externalWeatherMethods);

        String uri = "/weather/v1/current/" + countryCode + "/" + zip;

        ResultActions resultActions = mockMvc.perform(get(uri))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();


        org.springframework.http.ResponseEntity content = (org.springframework.http.ResponseEntity) mvcResult.getAsyncResult();
        CurrentWeatherResponse body = (CurrentWeatherResponse) content.getBody();
        assertTrue(body != null);
        assertTrue(body.getCurrent() != null);
        assertTrue(body.getCurrent().getDate().equals("2025-03-08 12:53:47"));

        assertTrue(body.getCurrent().getCurrentTemp() == 60.35f);
        assertTrue(body.getCurrent().getHighTemp() == 63.99f);
        assertTrue(body.getCurrent().getLowTemp() == 54.79f);
        assertTrue(!body.isCached());


    }

    @Test
    public void testExtended400() throws Exception {
        String zip = "12345";
        String countryCode = "US";

        Mono<CurrentWeatherAndResponseStatusDTO> mono = generateMono("open_weather_current_400.json", zip, countryCode);

        when(externalWeatherMethods.getCurrentWeather(zip, countryCode)).thenReturn(mono);
        weatherMethods = new WeatherMethodsImpl(externalWeatherMethods);

        String uri = "/weather/v1/current/" + countryCode + "/" + zip;


        ResultActions resultActions = mockMvc.perform(get(uri))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();

        org.springframework.http.ResponseEntity content = (org.springframework.http.ResponseEntity) mvcResult.getAsyncResult();
        CurrentWeatherResponse body = (CurrentWeatherResponse) content.getBody();

        assertTrue(content.getStatusCode() == HttpStatus.BAD_REQUEST);
        assertFalse(body.isCached());
        assertTrue(body.getCurrent() == null);
        assertTrue(StringUtils.isNotBlank(body.getMessage()));


    }

    @Test
    public void testExtended404() throws Exception {
        String zip = "94121";
        String countryCode = "US";

        Mono<CurrentWeatherAndResponseStatusDTO> mono = generateMono("open_weather_current_404.json", zip, countryCode);

        when(externalWeatherMethods.getCurrentWeather(zip, countryCode)).thenReturn(mono);
        weatherMethods = new WeatherMethodsImpl(externalWeatherMethods);

        String uri = "/weather/v1/current/" + countryCode + "/" + zip;


        ResultActions resultActions = mockMvc.perform(get(uri))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();

        org.springframework.http.ResponseEntity content = (org.springframework.http.ResponseEntity) mvcResult.getAsyncResult();
        CurrentWeatherResponse body = (CurrentWeatherResponse) content.getBody();


        assertFalse(body.isCached());
        assertTrue(content.getStatusCode() == HttpStatus.NOT_FOUND);
        assertFalse(body.isCached());
        assertTrue(body.getCurrent() == null);
        assertTrue(StringUtils.isNotBlank(body.getMessage()));


    }

    @Test
    public void testExtended429() throws Exception {

        String zip = "94121";
        String countryCode = "US";
        Mono<CurrentWeatherAndResponseStatusDTO> mono = generateMono("open_weather_current_429.json", zip, countryCode);

        when(externalWeatherMethods.getCurrentWeather(zip, countryCode)).thenReturn(mono);
        weatherMethods = new WeatherMethodsImpl(externalWeatherMethods);

        String uri = "/weather/v1/current/" + countryCode + "/" + zip;


        ResultActions resultActions = mockMvc.perform(get(uri))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();

        org.springframework.http.ResponseEntity content = (org.springframework.http.ResponseEntity) mvcResult.getAsyncResult();
        CurrentWeatherResponse body = (CurrentWeatherResponse) content.getBody();


        assertFalse(body.isCached());
        assertTrue(content.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS);
        assertFalse(body.isCached());
        assertTrue(body.getCurrent() == null);
        assertTrue(StringUtils.isNotBlank(body.getMessage()));

    }

    @Test
    public void testExtended500() throws Exception{
        String zip = "94121";
        String countryCode = "US";

        Mono<CurrentWeatherAndResponseStatusDTO> mono = generateMono("open_weather_current_500.json", zip, countryCode);

        when(externalWeatherMethods.getCurrentWeather(zip, countryCode)).thenReturn(mono);
        weatherMethods = new WeatherMethodsImpl(externalWeatherMethods);

        String uri = "/weather/v1/current/" + countryCode + "/" + zip;


        ResultActions resultActions = mockMvc.perform(get(uri))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();

        org.springframework.http.ResponseEntity content = (org.springframework.http.ResponseEntity) mvcResult.getAsyncResult();
        CurrentWeatherResponse body = (CurrentWeatherResponse) content.getBody();


        assertFalse(body.isCached());
        assertTrue(content.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);
        assertFalse(body.isCached());
        assertTrue(body.getCurrent() == null);
        assertTrue(StringUtils.isNotBlank(body.getMessage()));

    }


}
