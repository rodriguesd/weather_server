package com.stockheap.weather.service.weather;

import com.stockheap.weather.WeatherConstants;
import com.stockheap.weather.service.common.ExternalWeatherMethods;
import com.stockheap.weather.service.weather.dto.CurrentWeatherAndResponseStatusDTO;
import com.stockheap.weather.service.weather.dto.ExtendedWeatherAndResponseStatusDTO;
import com.stockheap.weather.util.ZipUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@AllArgsConstructor
@Service
public class WeatherMethodsImpl implements WeatherMethods {

    @Autowired
    private ExternalWeatherMethods externalWeatherMethods;


    /**
     * <p>
     *  This methods is used to get the current weather and will cache the result for a number of configured seconds
     *  the configuration parameter name is weather.expire.time.in.seconds
     * </p>
     * @param zip zip code/postal code
     * @param country this is the ios 3306 2 character representation (example: US (United States),MX (Mexico))
     * @return current weather, city, country, status, zip and error message
     */

    @Cacheable(unless = "#result == null or #result.statusCode != 200 or #result.current == null", value = WeatherConstants.CacheNames.WEATHER_FORECAST_CACHE, key = "'CACHE_ZIP_PREFIX_CURRENT_' + #zip.replace(' ','') + #country")
    public Mono<CurrentWeatherAndResponseStatusDTO> getCurrentWeather(String zip, String country) {
        if (isDataValid(zip, country)) {
            return Mono.fromCallable(() -> externalWeatherMethods.getCurrentWeather(zip.trim(), country))
                    .flatMap(mono -> mono);
        }
        return Mono.just(new CurrentWeatherAndResponseStatusDTO(HttpStatus.BAD_REQUEST.value(), false,"Invalid zip or country"));


    }


    /**
     * <p>
     *  This methods is used to get the extended weather and will cache the result for a number of configured seconds
     *  the configuration parameter name is weather.expire.time.in.seconds
     * </p>
     * @param zip zip code/postal code
     * @param country this is the ios 3306 2 character representation (example: US (United States),MX (Mexico))
     * @return extended weather, city, country, status, zip and error message
     */

    @Cacheable(unless = "#result == null or #result.statusCode != 200 or #result.extended == null or #result.extended.size() == 0", value = WeatherConstants.CacheNames.WEATHER_FORECAST_CACHE, key = "'CACHE_ZIP_PREFIX_EXT_' + #zip.replace(' ','')  + #country")
    public Mono<ExtendedWeatherAndResponseStatusDTO> getExtendedWeather(String zip, String country) {

        if (isDataValid(zip, country)) {
            return Mono.fromCallable(() -> externalWeatherMethods.getExtendedWeather(zip.trim(), country)).
                    flatMap(mono -> mono);
        }
        return Mono.just(new ExtendedWeatherAndResponseStatusDTO(HttpStatus.BAD_REQUEST.value(), false, "Invalid zip or country"));
    }


    private boolean isDataValid(String zip, String country) {

        return WeatherConstants.CountryCodes.isValidCode(country) &&
                ZipUtil.isValidZipCode(country, zip);
    }

}
