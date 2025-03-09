package com.stockheap.weather.functional;

import com.stockheap.weather.WeatherConstants;
import com.stockheap.weather.controller.WeatherController;
import com.stockheap.weather.controller.response.ExtendedWeatherResponse;
import com.stockheap.weather.data.common.dto.WeatherDataDTO;
import com.stockheap.weather.helpers.ResourceFileReaderSingleton;
import com.stockheap.weather.helpers.TestDataUtils;
import com.stockheap.weather.service.common.ExternalWeatherMethods;
import com.stockheap.weather.service.weather.dto.ExtendedWeatherAndResponseStatusDTO;
import com.stockheap.weather.service.weather.WeatherMethods;
import com.stockheap.weather.service.weather.WeatherMethodsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(WeatherController.class)
@Import({WeatherMethodsImpl.class})
class ExtendedWeatherControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExternalWeatherMethods externalWeatherMethods;

    @Autowired
    private WeatherMethods weatherMethods;


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



    private Mono<ExtendedWeatherAndResponseStatusDTO> generateMono(String fileName, String zip,
                                                                   String countryCode) throws Exception {


        String ok200 = ResourceFileReaderSingleton.getInstance().readFile(fileName);
        assertTrue(StringUtils.isNotBlank(ok200));
        return TestDataUtils.createCurrentExt(ok200, zip, countryCode);


    }


    @Test
    public void testCorrectData200() throws Exception {

        String zip = "94121";
        String countryCode = "US";
        Mono<ExtendedWeatherAndResponseStatusDTO> mono = generateMono("open_weather_ex_200.json", zip, countryCode);

        when(externalWeatherMethods.getExtendedWeather(zip, countryCode)).thenReturn(mono);
        weatherMethods = new WeatherMethodsImpl(externalWeatherMethods);

        String uri = "/weather/v1/extended/" + countryCode + "/" + zip;

        ResultActions resultActions = mockMvc.perform(get(uri))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();


        org.springframework.http.ResponseEntity content = (org.springframework.http.ResponseEntity) mvcResult.getAsyncResult();
        ExtendedWeatherResponse body = (ExtendedWeatherResponse) content.getBody();

        System.out.println(body);


        assertTrue(body != null);
        assertTrue(body.getExtended() != null);
        assertTrue(body.getExtended().size() > 0);
        WeatherDataDTO weatherDataDTO =  body.getExtended().get(0);
        assertTrue(weatherDataDTO != null);



        assertTrue(weatherDataDTO.getDate().equals("2025-03-08 16:00:00"));

        assertTrue(weatherDataDTO.getCurrentTemp() ==58.86f);
        assertTrue(weatherDataDTO.getHighTemp() == 58.86f);
        assertTrue(weatherDataDTO.getLowTemp() ==55.53f);
        assertTrue(!body.isCached());



    }



}
