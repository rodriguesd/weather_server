package com.stockheap.weather.service.external_weather.open_weather;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockheap.weather.data.common.dto.WeatherDataDTO;
import com.stockheap.weather.service.common.ExternalWeatherMethods;
import com.stockheap.weather.service.weather.dto.BaseWeatherAndResponseStatusDTO;
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

    @Value("${weather.expire.time.in.seconds}")
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


    /**
     * <p>
     *  This methods is used to get the current weather.
     * </p>
     * @param zip zip code/postal code
     * @param country this is the ios 3306 2 character representation (example: US (United States),MX (Mexico))
     * @return current weather, city, country, status, zip and error message
     */

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


    /**
     * <p>
     *  This methods is used to get the extended weather 30 days out.
     * </p>
     * @param zip zip code/postal code
     * @param country this is the ios 3306 2 character representation (example: US (United States),MX (Mexico))
     * @return extended weather, city, country, status, zip and error message
     */

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

        String message = "";
        int cod =0;
        String city ="";

        if(openExtendedWeatherResponse != null)
        {
            message = openExtendedWeatherResponse.getMessage();
            cod = openExtendedWeatherResponse.getCod();
            if(openExtendedWeatherResponse.getCity() != null)
            {
                city = openExtendedWeatherResponse.getCity().getName();
            }
        }
        initBaseWeatherAndResponseStatusDTO(zip,
                country,
                message,
                cod,
                city,
                weatherDataAndResponseStatusDTO);

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


    public void initBaseWeatherAndResponseStatusDTO(String zip,
                                                    String country,
                                                    String message,
                                                    int cod,
                                                    String city,
                                                    BaseWeatherAndResponseStatusDTO baseWeatherAndResponseStatusDTO)
    {
        baseWeatherAndResponseStatusDTO.setZip(zip);
        baseWeatherAndResponseStatusDTO.setCountryCode(country);
        baseWeatherAndResponseStatusDTO.setFromCache(false);
        baseWeatherAndResponseStatusDTO.setCity(city);

        if(StringUtils.isNotBlank(message))
        {
            baseWeatherAndResponseStatusDTO.setMessage(message);
        }
        baseWeatherAndResponseStatusDTO.setStatusCode(cod);
        if(cod == 0 && StringUtils.isBlank(message))
        {
            baseWeatherAndResponseStatusDTO.setStatusCode(HttpStatus.NO_CONTENT.value());
        }
        else {

            CodAndMessage codAndMessage =   getCodAndMessage(message);
            if(codAndMessage != null)
            {
                if(codAndMessage.getCod() > 0)
                {
                    baseWeatherAndResponseStatusDTO.setStatusCode(codAndMessage.getCod());
                }
                if(StringUtils.isNotBlank(codAndMessage.getMessage()))
                {
                    baseWeatherAndResponseStatusDTO.setMessage(codAndMessage.getMessage());
                }
            }
        }

        baseWeatherAndResponseStatusDTO.setFromCache(false);
    }


    public CurrentWeatherAndResponseStatusDTO createWeatherDataAndResponseStatusDTO(String zip,
                                                                                    String country,
                                                                                    String units,
                                                                                    OpenWeatherResponse openWeatherResponse) {
        CurrentWeatherAndResponseStatusDTO weatherDataAndResponseStatusDTO = new CurrentWeatherAndResponseStatusDTO();
        if (openWeatherResponse.isValid() && openWeatherResponse.getCod() == HttpStatus.OK.value()) {

            if (openWeatherResponse.getMain() != null) {
                WeatherDataDTO weatherDataDTO = createWeatherData(openWeatherResponse.getMain(),
                        units, openWeatherResponse.getDt(), openWeatherResponse.getTimezone());
                if (weatherDataDTO != null) {
                    weatherDataAndResponseStatusDTO.setCurrent(weatherDataDTO);
                }
            }
        }

        String message = openWeatherResponse.getMessage();
        int cod = openWeatherResponse.getCod();
        initBaseWeatherAndResponseStatusDTO(zip,
                country,
                message,
                cod,
                openWeatherResponse.getName(),
                weatherDataAndResponseStatusDTO);

        return weatherDataAndResponseStatusDTO;
    }


    private static boolean isValidJson(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readTree(jsonString);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }


    private CodAndMessage getCodAndMessage(String jsonString)
    {

        try {

            if(!isValidJson(jsonString))
            {
                return null;
            }


            JsonNode rootNode = MAPPER.readTree(jsonString);

            if(rootNode != null &&
                    rootNode.get("cod") != null &&
                    rootNode.get("message") != null)
            {
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
            }


        } catch (Exception e) {
                e.printStackTrace();
        }

        return null;

    }




}
