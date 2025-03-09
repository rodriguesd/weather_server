package com.stockheap.weather.service.external_weather.open_weather;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockheap.weather.data.common.dto.WeatherDataDTO;
import com.stockheap.weather.service.common.ExternalWeatherMethods;
import com.stockheap.weather.service.weather.dto.CurrentWeatherAndResponseStatusDTO;
import com.stockheap.weather.service.weather.dto.ExtendedWeatherAndResponseStatusDTO;
import com.stockheap.weather.service.external_weather.open_weather.response_data.*;
import com.stockheap.weather.util.DateUtil;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
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


    private class CodAndMessage
    {
        private int cod = 0;
        private String message ="";

        public CodAndMessage(int cod, String message) {
            this.cod = cod;
            this.message = message;
        }

        public int getCod() {
            return cod;
        }

        public String getMessage() {
            return message;
        }
    }


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

        String upperCaseCountryCode =  country.toUpperCase();
        String zipCodeEncoded = URLEncoder.encode(zip, StandardCharsets.UTF_8);
        String encodedCountry = URLEncoder.encode(upperCaseCountryCode, StandardCharsets.UTF_8);


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

    public Mono<CurrentWeatherAndResponseStatusDTO> getCurrentWeather(String zip, String country) {

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
                        return clientResponse.bodyToMono(String.class)
                                .map(jsonString -> {

                                    return new OpenWeatherResponse(false, HttpStatus.BAD_REQUEST.value(), jsonString);
                                });
                    }
                }).flatMap(openWeatherResponse -> {
                    return Mono.just(createWeatherDataAndResponseStatusDTO(zip, country, IMPERIAL, openWeatherResponse));
                });
    }


    public Mono<ExtendedWeatherAndResponseStatusDTO> getExtendedWeather(String zip, String country) {
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
                    }
                    else {
                        return clientResponse.bodyToMono(String.class)
                                .map(jsonString -> {

                                    return new OpenExtendedWeatherResponse(false,  HttpStatus.BAD_REQUEST.value(), jsonString);
                                });
                    }
                }).flatMap(openExtendedWeatherResponse -> {
                    return Mono.just(createWeatherDataAndResponseStatusDTO(zip, country, IMPERIAL, openExtendedWeatherResponse));
                });
    }


    public ExtendedWeatherAndResponseStatusDTO createWeatherDataAndResponseStatusDTO(String zip,
                                                                                     String country,
                                                                                     String units,
                                                                                     OpenExtendedWeatherResponse openExtendedWeatherResponse) {
        ExtendedWeatherAndResponseStatusDTO weatherDataAndResponseStatusDTO = new ExtendedWeatherAndResponseStatusDTO();
        weatherDataAndResponseStatusDTO.setZip(zip);
        weatherDataAndResponseStatusDTO.setCountryCode(country);
        weatherDataAndResponseStatusDTO.setFromCache(false);
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
                        WeatherDataDTO weatherDataDTO = createWeatherData(openWeatherResponse.getMain(),
                                units, openWeatherResponse.getDt(), timeZone);
                        if (weatherDataDTO != null) {
                            weatherDataAndResponseStatusDTO.addWeatherData(weatherDataDTO);
                        }
                    }
                }
            }
        }

        if (openExtendedWeatherResponse != null) {

            if(StringUtils.isNotBlank(openExtendedWeatherResponse.getMessage()))
            {
                weatherDataAndResponseStatusDTO.setMessage(openExtendedWeatherResponse.getMessage());
            }

            weatherDataAndResponseStatusDTO.setStatusCode(openExtendedWeatherResponse.getCod());
            if(openExtendedWeatherResponse.getCod() == 0 &&
                    StringUtils.isBlank(openExtendedWeatherResponse.getMessage()))
            {
                weatherDataAndResponseStatusDTO.setStatusCode(HttpStatus.NO_CONTENT.value());
            }
            else {


                CodAndMessage codAndMessage =   getCodAndMessage(openExtendedWeatherResponse.getMessage());
                if(codAndMessage != null)
                {
                    if(codAndMessage.getCod() > 0)
                    {
                        weatherDataAndResponseStatusDTO.setStatusCode(codAndMessage.getCod());
                    }
                    if(StringUtils.isNotBlank(codAndMessage.getMessage()))
                    {
                        weatherDataAndResponseStatusDTO.setMessage(codAndMessage.getMessage());
                    }
                }
            }

            weatherDataAndResponseStatusDTO.setFromCache(false);
        }

        return weatherDataAndResponseStatusDTO;

    }

    private WeatherDataDTO createWeatherData(Main main, String units, Long epochInSeconds, Long timeZone) {
        if (main != null) {
            WeatherDataDTO weatherDataDTO = new WeatherDataDTO();
            weatherDataDTO.setHighTemp(main.getTempMax());
            weatherDataDTO.setLowTemp(main.getTempMin());
            weatherDataDTO.setUnits(units);
            weatherDataDTO.setDate(DateUtil.convertToLocalTime(epochInSeconds, timeZone));
            weatherDataDTO.setCurrentTemp(main.getTemp());
            return weatherDataDTO;
        }
        return null;

    }


    public CurrentWeatherAndResponseStatusDTO createWeatherDataAndResponseStatusDTO(String zip,
                                                                                     String country,
                                                                                     String units,
                                                                                     OpenWeatherResponse openWeatherResponse) {
        CurrentWeatherAndResponseStatusDTO weatherDataAndResponseStatusDTO = new CurrentWeatherAndResponseStatusDTO();
        weatherDataAndResponseStatusDTO.setZip(zip);
        weatherDataAndResponseStatusDTO.setCountryCode(country);
        weatherDataAndResponseStatusDTO.setFromCache(false);
        if (openWeatherResponse.isValid() && openWeatherResponse.getCod() == HttpStatus.OK.value()) {

            if (openWeatherResponse.getMain() != null) {
                WeatherDataDTO weatherDataDTO = createWeatherData(openWeatherResponse.getMain(),
                        units, openWeatherResponse.getDt(), openWeatherResponse.getTimezone());
                if (weatherDataDTO != null) {
                    weatherDataAndResponseStatusDTO.setCurrent(weatherDataDTO);
                }
            }
        }
        if (openWeatherResponse != null) {

            if(StringUtils.isNotBlank(openWeatherResponse.getMessage()))
            {
                weatherDataAndResponseStatusDTO.setMessage(openWeatherResponse.getMessage());
            }
            weatherDataAndResponseStatusDTO.setStatusCode(openWeatherResponse.getCod());
            if(openWeatherResponse.getCod() == 0 && StringUtils.isBlank(openWeatherResponse.getMessage()))
            {
                weatherDataAndResponseStatusDTO.setStatusCode(HttpStatus.NO_CONTENT.value());
            }
            else {

                CodAndMessage codAndMessage =   getCodAndMessage(openWeatherResponse.getMessage());
                if(codAndMessage != null)
                {
                    if(codAndMessage.getCod() > 0)
                    {
                        weatherDataAndResponseStatusDTO.setStatusCode(codAndMessage.getCod());
                    }
                    if(StringUtils.isNotBlank(codAndMessage.getMessage()))
                    {
                        weatherDataAndResponseStatusDTO.setMessage(codAndMessage.getMessage());
                    }
                }
            }

            weatherDataAndResponseStatusDTO.setFromCache(false);
        }

        return weatherDataAndResponseStatusDTO;
    }



    private CodAndMessage getCodAndMessage(String jsonString)
    {

        try {
            JsonNode rootNode = MAPPER.readTree(jsonString);

            String cod = rootNode.get("cod").asText();
            String message = rootNode.get("message").asText();

            if(StringUtils.isNotBlank(cod) && StringUtils.isNotBlank(message))
            {
                return new CodAndMessage(Integer.parseInt(cod), message);
            }

            if(StringUtils.isNotBlank(cod) )
            {
                return new CodAndMessage(Integer.parseInt(cod), "");
            }

            if(StringUtils.isNotBlank(message) )
            {
                return new CodAndMessage(0, message);
            }

        } catch (Exception e) {
                e.printStackTrace();
        }

        return null;

    }




}
