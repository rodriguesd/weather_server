package com.stockheap.weather.service.external_weather.open_weather;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockheap.weather.data.common.dto.WeatherData;
import com.stockheap.weather.service.external_weather.common.ExternalWeatherMethods;
import com.stockheap.weather.service.external_weather.dto.WeatherDataAndResponseStatusDTO;
import com.stockheap.weather.service.external_weather.open_weather.response_data.Main;
import com.stockheap.weather.service.external_weather.open_weather.response_data.OpenExtendedWeatherResponse;
import com.stockheap.weather.service.external_weather.open_weather.response_data.OpenWeatherResponse;
import com.stockheap.weather.service.external_weather.open_weather.response_data.Sys;
import com.stockheap.weather.util.DateUtil;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
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

    private String generateUrl(String zip, String country, String url) {

        String zipCodeEncoded = URLEncoder.encode(zip, StandardCharsets.UTF_8);
        String encodedCountry = URLEncoder.encode(country, StandardCharsets.UTF_8);

        return UriComponentsBuilder.fromHttpUrl(url)
                .queryParam(ZIP, zipCodeEncoded + "," + encodedCountry)
                .queryParam(LIMIT, LIMIT_VALUE.toString())
                .queryParam(APP_ID, openWeatherApiKey)
                .queryParam(UNITS, IMPERIAL)
                .toUriString();
    }

    public Mono<WeatherDataAndResponseStatusDTO> getCurrentWeather(String zip, String country) {

        final String updatedUrl = generateUrl(zip, country, openWeatherCurrentWeatherUrl);
        System.out.println(updatedUrl);
        WebClient webClient = create();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(updatedUrl)
                        .build()).exchangeToMono(clientResponse -> {
                    HttpStatusCode httpStatusCode = clientResponse.statusCode(); // Get response status
                    if (httpStatusCode.is2xxSuccessful()) {
                        return clientResponse.bodyToMono(OpenWeatherResponse.class);
                    } else {
                        return Mono.just(new OpenWeatherResponse(false, httpStatusCode));
                    }
                }).flatMap(openWeatherResponse -> {
                    return Mono.just(createWeatherDataAndResponseStatusDTO(zip, country, IMPERIAL, openWeatherResponse));
                });
    }


    public Mono<WeatherDataAndResponseStatusDTO> getExtendedWeather(String zip, String country) {

        String updatedUrl = generateUrl(zip, country, openWeatherExtendedWeatherUrl);
        WebClient webClient = create();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(updatedUrl)
                        .build()).exchangeToMono(clientResponse -> {
                    HttpStatusCode httpStatusCode = clientResponse.statusCode(); // Get response status
                    if (httpStatusCode.is2xxSuccessful()) {
                        return clientResponse.bodyToMono(OpenExtendedWeatherResponse.class);
                    } else {
                        return Mono.just(new OpenExtendedWeatherResponse(false, httpStatusCode));
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

        if (openExtendedWeatherResponse.isValid() && openExtendedWeatherResponse.getCod() == 200) {

            if (openExtendedWeatherResponse.getList() != null &&
                    openExtendedWeatherResponse.getList().size() > 0) {

                for (OpenWeatherResponse openWeatherResponse : openExtendedWeatherResponse.getList()) {
                    if (openWeatherResponse != null &&
                            openWeatherResponse.getMain() != null) {
                        WeatherData weatherData = createWeatherData(openWeatherResponse.getMain(),
                                IMPERIAL, openWeatherResponse.getDt(), openWeatherResponse.getTimezone());
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
