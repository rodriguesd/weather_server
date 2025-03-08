package com.stockheap.weather.util;

import com.stockheap.weather.WeatherConstants;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZipUtil {
    private ZipUtil() {

    }

    private static final Map<String, String> ZIP_CODES_PATTERNS = new HashMap<>();

    static {
        ZIP_CODES_PATTERNS.put("US", "^\\d{5}(-\\d{4})?$");
        ZIP_CODES_PATTERNS.put("CA", "^[A-Z\\d]{3}\\s?[A-Z\\d]{3}$");
        ZIP_CODES_PATTERNS.put("GB", "^([A-Z\\d]{2}[\\s\\d][A-Z\\d]?){1,2}[\\s\\d][A-Z\\d]{2}$");
        ZIP_CODES_PATTERNS.put("DE", "^\\d{5}$");
        ZIP_CODES_PATTERNS.put("MX", "^[0-9]{5}$");
        ZIP_CODES_PATTERNS.put("PT", "^[0-9]{4}-[0-9]{3}$");
        ZIP_CODES_PATTERNS.put("ES", "^[0-9]{5}$");
    }

    public static boolean isValidZipCode(String countryCode, String zipCode) {
        String pattern = ZIP_CODES_PATTERNS.get(countryCode);
        if (pattern == null) {
            return false; // Country code not supported
        }
        Pattern zipPattern = Pattern.compile(pattern);
        Matcher matcher = zipPattern.matcher(zipCode);
        return matcher.matches();
    }


    public static boolean validateZip(String countryCodes, String zip) {

        if (StringUtils.isNotBlank(countryCodes) && StringUtils.isNotBlank(countryCodes)) {
            boolean ok = WeatherConstants.CountryCodes.isValidCode(countryCodes.toUpperCase());
            if (ok) {
                return isValidZipCode(countryCodes.toUpperCase(), zip);
            }
        }
        return false;
    }
}
