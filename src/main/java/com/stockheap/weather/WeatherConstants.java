package com.stockheap.weather;

import java.util.HashMap;
import java.util.Map;

public class WeatherConstants {
    public static class CacheNames {
        public static final String WEATHER_FORECAST_CACHE = "WEATHER_FORECAST_CACHE";
    }

    public static class CountryCodes {


        private  static Map<String, String> COUNTRY_MAP = new HashMap<>();

        static {
            // North America
            COUNTRY_MAP.put("US","United States");
            COUNTRY_MAP.put("CA","Canada");
            COUNTRY_MAP.put("GB","United Kingdom");
            COUNTRY_MAP.put("DE","Germany");

            COUNTRY_MAP.put("MX","Mexico");
            COUNTRY_MAP.put("PT","Portugal");

            COUNTRY_MAP.put("ES","Spain");
        }


        public static boolean isValidCode(String  code)
        {
            if(code != null)
            {
                return COUNTRY_MAP.containsKey(code.trim().toUpperCase());
            }
            return false;
        }
    }



}
