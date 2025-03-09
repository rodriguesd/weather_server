package com.stockheap.weather.controller;


import com.stockheap.weather.controller.response.CurrentWeatherResponse;
import com.stockheap.weather.controller.response.ExtendedWeatherResponse;
import com.stockheap.weather.service.weather.WeatherMethods;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/weather")
public class WeatherController {



    private final WeatherMethods weatherMethods;

    @Autowired
    public WeatherController(WeatherMethods weatherMethods) {
        this.weatherMethods = weatherMethods;
    }




    @Operation(summary = "Gets the current weather by zip and country code", parameters = {
            @Parameter(name = "countryCode", description = "supported countries (US (United States), CA (Canada), GB (United Kingdom), DE (Germany), MX (Mexico), PT (Portugal), ES (Spain)) use two character value in the API", required = true),
            @Parameter(name = "zip", description = "a valid zip code for the country code", required = true)
    })
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Successfully retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CurrentWeatherResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request. You can get 400 error if either some mandatory parameters in the request are missing or some of request parameters have incorrect format or values out of allowed range. List of all parameters names that are missing or incorrect"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found. You can get 404 error if data with requested parameters (zip country code) does not exist in service database."),
            @ApiResponse(responseCode = "500", description = "Unexpected Error. You can get '5xx' error in case of other internal errors."),
    })




    @GetMapping("/v1/current/{countryCode}/{zip}")
    public Mono<ResponseEntity<CurrentWeatherResponse>> current(@PathVariable("countryCode") @NotBlank @Parameter(name = "countryCode", description = "country code", example = "US")  String countryCode,
                                                                @PathVariable("zip")  @NotBlank @Parameter(name = "zip", description = "zip/postal code", example = "94121") String zip) {
        return weatherMethods.getCurrentWeather(zip, countryCode)
                .map(weatherDataAndResponseStatusDTO -> {
                    if (weatherDataAndResponseStatusDTO != null && weatherDataAndResponseStatusDTO.ok()) {
                        CurrentWeatherResponse currentWeatherResponse = new CurrentWeatherResponse(weatherDataAndResponseStatusDTO.getCurrent(),
                                                weatherDataAndResponseStatusDTO.isFromCache(), weatherDataAndResponseStatusDTO.getMessage());
                        return ResponseEntity.ok(currentWeatherResponse);
                    } else {
                        if(weatherDataAndResponseStatusDTO != null)
                        {
                            return ResponseEntity.status(HttpStatus.resolve(weatherDataAndResponseStatusDTO.getStatusCode().intValue())).body(new CurrentWeatherResponse(weatherDataAndResponseStatusDTO.getCurrent(),
                                   false, weatherDataAndResponseStatusDTO.getMessage()));
                        }
                        return ResponseEntity.status(HttpStatus.resolve(weatherDataAndResponseStatusDTO.getStatusCode().intValue())).body(new CurrentWeatherResponse());
                    }
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CurrentWeatherResponse()));
    }



    @Operation(summary = "Gets the extended weather by zip and country code", parameters = {
            @Parameter(name = "countryCode", description = "supported countries (US (United States), CA (Canada), GB (United Kingdom), DE (Germany), MX (Mexico), PT (Portugal), ES (Spain)) use two character value in the API", required = true),
            @Parameter(name = "zip", description = "a valid zip code for the country code", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExtendedWeatherResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request. You can get 400 error if either some mandatory parameters in the request are missing or some of request parameters have incorrect format or values out of allowed range. List of all parameters names that are missing or incorrect"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found. You can get 404 error if data with requested parameters (zip country code) does not exist in service database."),
            @ApiResponse(responseCode = "500", description = "Unexpected Error. You can get '5xx' error in case of other internal errors."),
    })


    @GetMapping("/v1/extended/{countryCode}/{zip}")
    public Mono<ResponseEntity<ExtendedWeatherResponse>> extended(@PathVariable("countryCode") @NotBlank @Parameter(name = "countryCode", description = "country code", example = "US")  String countryCode,
                                                                  @PathVariable("zip")  @NotBlank @Parameter(name = "zip", description = "zip/postal code", example = "94121") String zip) {

        return weatherMethods.getExtendedWeather(zip, countryCode)
                .map(weatherDataAndResponseStatusDTO -> {
                    if (weatherDataAndResponseStatusDTO != null && weatherDataAndResponseStatusDTO.ok()) {
                        ExtendedWeatherResponse extendedWeatherResponse = new ExtendedWeatherResponse(weatherDataAndResponseStatusDTO.getExtended(), weatherDataAndResponseStatusDTO.isFromCache(), weatherDataAndResponseStatusDTO.getMessage());
                        return ResponseEntity.ok(extendedWeatherResponse);
                    } else {
                        if(weatherDataAndResponseStatusDTO != null)
                        {
                            ExtendedWeatherResponse extendedWeatherResponse = new ExtendedWeatherResponse(weatherDataAndResponseStatusDTO.getExtended(), false, weatherDataAndResponseStatusDTO.getMessage());
                            return ResponseEntity.ok(extendedWeatherResponse);
                        }
                        return ResponseEntity.status(HttpStatus.resolve(weatherDataAndResponseStatusDTO.getStatusCode().intValue())).body(new ExtendedWeatherResponse());
                    }
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExtendedWeatherResponse()));
    }


}
