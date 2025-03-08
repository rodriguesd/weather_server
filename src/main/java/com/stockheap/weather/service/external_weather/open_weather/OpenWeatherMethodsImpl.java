package com.stockheap.weather.service.external_weather.open_weather;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockheap.weather.data.common.dto.WeatherData;
import com.stockheap.weather.service.external_weather.common.ExternalWeatherMethods;
import com.stockheap.weather.service.external_weather.dto.WeatherDataAndResponseStatusDTO;
import com.stockheap.weather.service.external_weather.open_weather.response_data.*;
import com.stockheap.weather.util.DateUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpenWeatherMethodsImpl implements ExternalWeatherMethods {


    @Autowired
    private WebClient webClient;

    @Value("${open.weather.api.key}")
    private String openWeatherApiKey;


    @Value("${open.weather.city.country.current.weather.url}")
    private String openWeatherCurrentWeatherUrl;


    @Value("${open.weather.city.country.extended.weather.url}")
    private String openWeatherExtendedWeatherUrl;

    @Value("${open.weather.base.url}")
    private String openWeatherBaseUrl;

    @Value("${open.weather.expire.time.in.seconds}")
    private long openWeatherCacheExpire;


    private static final String LIMIT = "limit";
    private static final String ZIP = "zip";
    private static final String APP_ID = "appid";
    private static final Integer LIMIT_VALUE = 1;
    private static final String UNITS = "units";

    private static final String IMPERIAL = "imperial";
    private static ObjectMapper MAPPER = new ObjectMapper();

    @PostConstruct
    private void init() {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private MultiValueMap<String, String> generateParams(String zip, String country) {

        String zipCodeEncoded = URLEncoder.encode(zip, StandardCharsets.UTF_8);
        String encodedCountry = URLEncoder.encode(country, StandardCharsets.UTF_8);


        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        List<String> zipValues = new ArrayList<>();
        zipValues.add(zipCodeEncoded + "," + encodedCountry);
        params.put(ZIP, zipValues);

        List<String> limitValues = new ArrayList<>();
        limitValues.add(LIMIT_VALUE.toString());
        params.put(LIMIT, limitValues);

        List<String> apiKeyValues = new ArrayList<>();
        apiKeyValues.add(openWeatherApiKey);
        params.put(APP_ID, apiKeyValues);

        List<String> unitsValues = new ArrayList<>();
        unitsValues.add(IMPERIAL);
        params.put(UNITS, unitsValues);
        return params;
    }

    public Mono<WeatherDataAndResponseStatusDTO> getCurrentWeather(String zip, String country) {

        MultiValueMap<String, String> params = generateParams(zip, country);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(openWeatherCurrentWeatherUrl).queryParams(params)
                        .build()).exchangeToMono(clientResponse -> {
                    HttpStatusCode httpStatusCode = clientResponse.statusCode(); // Get response status
                    if (httpStatusCode.is2xxSuccessful()) {
                        return clientResponse.bodyToMono(String.class).map(jsonString -> {
                            try {
                                return MAPPER.readValue(jsonString, OpenWeatherResponse.class);
                            } catch (Exception e) {
                                return new OpenWeatherResponse(false, HttpStatus.NO_CONTENT.value());
                            }
                        });
                    } else {
                        return Mono.just(new OpenWeatherResponse(false, httpStatusCode.value()));
                    }
                }).flatMap(openWeatherResponse -> {
                    return Mono.just(createWeatherDataAndResponseStatusDTO(zip, country, IMPERIAL, openWeatherResponse));
                });
    }


    public Mono<WeatherDataAndResponseStatusDTO> getExtendedWeather(String zip, String country) {
        MultiValueMap<String, String> params = generateParams(zip, country);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(openWeatherExtendedWeatherUrl).queryParams(params)
                        .build()).exchangeToMono(clientResponse -> {
                    HttpStatusCode httpStatusCode = clientResponse.statusCode(); // Get response status
                    if (httpStatusCode.is2xxSuccessful()) {
                        return clientResponse.bodyToMono(String.class)
                                .map(jsonString -> {
                                    try {
                                        return MAPPER.readValue(jsonString, OpenExtendedWeatherResponse.class);
                                    } catch (Exception e) {
                                        return new OpenExtendedWeatherResponse(false,  HttpStatus.NO_CONTENT.value());
                                    }
                                });
                    } else {
                        return Mono.just(new OpenExtendedWeatherResponse(false, httpStatusCode.value()));
                    }
                }).flatMap(openExtendedWeatherResponse -> {
                    return Mono.just(createWeatherDataAndResponseStatusDTO(zip, country, IMPERIAL, openExtendedWeatherResponse));
                });
    }


    private WeatherDataAndResponseStatusDTO createWeatherDataAndResponseStatusDTO(String zip,
                                                                                  String country,
                                                                                  String units,
                                                                                  OpenExtendedWeatherResponse openExtendedWeatherResponse) {


        WeatherDataAndResponseStatusDTO weatherDataAndResponseStatusDTO = new WeatherDataAndResponseStatusDTO();
        weatherDataAndResponseStatusDTO.setZip(zip);
        weatherDataAndResponseStatusDTO.setZip(country);

        long timeZone = 0;
        if (openExtendedWeatherResponse != null &&
                openExtendedWeatherResponse.getCity() != null) {
            timeZone = openExtendedWeatherResponse.getCity().getTimezone();
        }


        if (openExtendedWeatherResponse != null &&
                openExtendedWeatherResponse.isValid() &&
                openExtendedWeatherResponse.getCod() == HttpStatus.OK.value()) {

            if (openExtendedWeatherResponse.getList() != null &&
                    openExtendedWeatherResponse.getList().size() > 0) {

                for (MainAndDateTime openWeatherResponse : openExtendedWeatherResponse.getList()) {
                    if (openWeatherResponse != null &&
                            openWeatherResponse.getMain() != null) {
                        WeatherData weatherData = createWeatherData(openWeatherResponse.getMain(),
                                units, openWeatherResponse.getDt(), timeZone);
                        if (weatherData != null) {
                            weatherDataAndResponseStatusDTO.addWeatherData(weatherData);
                        }
                    }
                }
            }
        }

        if (openExtendedWeatherResponse != null) {

            weatherDataAndResponseStatusDTO.setStatusCode(openExtendedWeatherResponse.getCod());
            if(openExtendedWeatherResponse.getCod() == 0)
            {
                weatherDataAndResponseStatusDTO.setStatusCode(HttpStatus.NO_CONTENT.value());
            }
            weatherDataAndResponseStatusDTO.setFromCache(false);
        }

        return weatherDataAndResponseStatusDTO;

    }

    private WeatherData createWeatherData(Main main, String units, Long epochInSeconds, Long timeZone) {
        if (main != null) {
            WeatherData weatherData = new WeatherData();
            weatherData.setHighTemp(main.getTempMax());
            weatherData.setLowTemp(main.getTempMin());
            weatherData.setUnits(units);
            weatherData.setDate(DateUtil.convertToLocalTime(epochInSeconds, timeZone));
            weatherData.setCurrentTemp(main.getTemp());
            return weatherData;
        }
        return null;

    }


    private WeatherDataAndResponseStatusDTO createWeatherDataAndResponseStatusDTO(String zip,
                                                                                  String country,
                                                                                  String units,
                                                                                  OpenWeatherResponse openWeatherResponse) {
        WeatherDataAndResponseStatusDTO weatherDataAndResponseStatusDTO = new WeatherDataAndResponseStatusDTO();
        weatherDataAndResponseStatusDTO.setZip(zip);
        weatherDataAndResponseStatusDTO.setZip(country);
        if (openWeatherResponse.isValid() && openWeatherResponse.getCod() == HttpStatus.OK.value()) {

            if (openWeatherResponse.getMain() != null) {
                WeatherData weatherData = createWeatherData(openWeatherResponse.getMain(),
                        units, openWeatherResponse.getDt(), openWeatherResponse.getTimezone());
                if (weatherData != null) {
                    weatherDataAndResponseStatusDTO.setCurrent(weatherData);
                }
            }
        }
        if (openWeatherResponse != null) {
            weatherDataAndResponseStatusDTO.setStatusCode(openWeatherResponse.getCod());
            if(openWeatherResponse.getCod() == 0)
            {
                weatherDataAndResponseStatusDTO.setStatusCode(HttpStatus.NO_CONTENT.value());
            }
            weatherDataAndResponseStatusDTO.setFromCache(false);
        }

        return weatherDataAndResponseStatusDTO;
    }




}
