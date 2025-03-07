package com.stockheap.weather.service.external_weather.open_weather;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockheap.weather.data.common.dto.WeatherData;
import com.stockheap.weather.service.external_weather.common.ExternalWeatherMethods;
import com.stockheap.weather.service.external_weather.dto.WeatherDataAndResponseStatusDTO;
import com.stockheap.weather.service.external_weather.open_weather.response_data.*;
import com.stockheap.weather.util.DateUtil;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OpenWeatherMethodsImpl implements ExternalWeatherMethods {


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
        WebClient webClient = create();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(openWeatherCurrentWeatherUrl).queryParams(params)
                        .build()).exchangeToMono(clientResponse -> {
                    HttpStatusCode httpStatusCode = clientResponse.statusCode(); // Get response status
                    if (httpStatusCode.is2xxSuccessful()) {
                        return clientResponse.bodyToMono(String.class).map(jsonString -> {
                            try {
                                return MAPPER.readValue(jsonString, OpenWeatherResponse.class);
                            } catch (Exception e) {
                                return new OpenWeatherResponse(false, HttpStatusCode.valueOf(666));
                            }
                        });
                    } else {
                        return Mono.just(new OpenWeatherResponse(false, httpStatusCode));
                    }
                }).flatMap(openWeatherResponse -> {
                    return Mono.just(createWeatherDataAndResponseStatusDTO(zip, country, IMPERIAL, openWeatherResponse));
                });
    }


    public Mono<WeatherDataAndResponseStatusDTO> getExtendedWeather(String zip, String country) {
        MultiValueMap<String, String> params = generateParams(zip, country);
        WebClient webClient = create();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(openWeatherExtendedWeatherUrl).queryParams(params)
                        .build()).exchangeToMono(clientResponse -> {
                    HttpStatusCode httpStatusCode = clientResponse.statusCode(); // Get response status
                    if (httpStatusCode.is2xxSuccessful()) {
                        return clientResponse.bodyToMono(String.class)
                                .map(jsonString -> {
                                    try {
                                        String a1 ="";
                                        return MAPPER.readValue(jsonString, OpenExtendedWeatherResponse.class);
                                    } catch (Exception e) {
                                        return new OpenExtendedWeatherResponse(false, HttpStatusCode.valueOf(666));
                                    }
                                });
                    } else {
                        return Mono.just(new OpenExtendedWeatherResponse(false, httpStatusCode));
                    }
                }).flatMap(openExtendedWeatherResponse -> {
                    String a1 = "";
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
        if(openExtendedWeatherResponse != null &&
                openExtendedWeatherResponse.getCity() != null)
        {
            timeZone = openExtendedWeatherResponse.getCity().getTimezone();
        }


        if (openExtendedWeatherResponse.isValid() && openExtendedWeatherResponse.getCod() == 200) {

            if (openExtendedWeatherResponse.getList() != null &&
                    openExtendedWeatherResponse.getList().size() > 0) {

                for (MainAndDateTime openWeatherResponse : openExtendedWeatherResponse.getList()) {
                    if (openWeatherResponse != null &&
                            openWeatherResponse.getMain() != null) {
                        WeatherData weatherData = createWeatherData(openWeatherResponse.getMain(),
                                IMPERIAL, openWeatherResponse.getDt(),timeZone);
                        if (weatherData != null) {
                            weatherDataAndResponseStatusDTO.addWeatherData(weatherData);
                        }
                    }
                }
            }

        }

        weatherDataAndResponseStatusDTO.setHttpStatusCode(openExtendedWeatherResponse.getHttpStatusCode());
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
        if (openWeatherResponse.isValid() && openWeatherResponse.getCod() == 200) {

            if (openWeatherResponse.getMain() != null) {
                WeatherData weatherData = createWeatherData(openWeatherResponse.getMain(),
                        IMPERIAL, openWeatherResponse.getDt(), openWeatherResponse.getTimezone());
                if (weatherData != null) {
                    weatherDataAndResponseStatusDTO.setCurrent(weatherData);
                }
            }
        }
        weatherDataAndResponseStatusDTO.setHttpStatusCode(openWeatherResponse.getHttpStatusCode());
        return weatherDataAndResponseStatusDTO;
    }

    private WebClient create() {

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
        return WebClient.builder()
                .baseUrl(openWeatherBaseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

    }


}
