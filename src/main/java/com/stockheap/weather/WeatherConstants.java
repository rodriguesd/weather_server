package com.stockheap.weather;

import java.util.Map;

public class WeatherConstants {
    public static class CacheNames {
        public static final String WEATHER_FORECAST_CACHE = "WEATHER_FORECAST_CACHE";
    }

    public static class ErrorCodes {
        public static final int INVALID_DATA_CURRENT = 666;
        public static final int INVALID_DATA_EXTENDED = 667;
        public static final int INVALID_DATA = 668;

    }

    public static class CountryCodes
    {



        private static final Map<String, String> COUNTRY_MAP = Map.ofEntries(
                Map.entry("AF", "Afghanistan"),
                Map.entry("AX", "Aland Islands"),
                Map.entry("AL", "Albania"),
                Map.entry("DZ", "Algeria"),
                Map.entry("AS", "American Samoa"),
                Map.entry("AD", "Andorra"),
                Map.entry("AO", "Angola"),
                Map.entry("AI", "Anguilla"),
                Map.entry("AQ", "Antarctica"),
                Map.entry("AG", "Antigua And Barbuda"),
                Map.entry("AR", "Argentina"),
                Map.entry("AM", "Armenia"),
                Map.entry("AW", "Aruba"),
                Map.entry("AU", "Australia"),
                Map.entry("AT", "Austria"),
                Map.entry("AZ", "Azerbaijan"),
                Map.entry("BS", "Bahamas"),
                Map.entry("BH", "Bahrain"),
                Map.entry("BD", "Bangladesh"),
                Map.entry("BB", "Barbados"),
                Map.entry("BY", "Belarus"),
                Map.entry("BE", "Belgium"),
                Map.entry("BZ", "Belize"),
                Map.entry("BJ", "Benin"),
                Map.entry("BM", "Bermuda"),
                Map.entry("BT", "Bhutan"),
                Map.entry("BO", "Bolivia"),
                Map.entry("BA", "Bosnia And Herzegovina"),
                Map.entry("BW", "Botswana"),
                Map.entry("BV", "Bouvet Island"),
                Map.entry("BR", "Brazil"),
                Map.entry("IO", "British Indian Ocean Territory"),
                Map.entry("BN", "Brunei Darussalam"),
                Map.entry("BG", "Bulgaria"),
                Map.entry("BF", "Burkina Faso"),
                Map.entry("BI", "Burundi"),
                Map.entry("KH", "Cambodia"),
                Map.entry("CM", "Cameroon"),
                Map.entry("CA", "Canada"),
                Map.entry("CV", "Cape Verde"),
                Map.entry("KY", "Cayman Islands"),
                Map.entry("CF", "Central African Republic"),
                Map.entry("TD", "Chad"),
                Map.entry("CL", "Chile"),
                Map.entry("CN", "China"),
                Map.entry("CX", "Christmas Island"),
                Map.entry("CC", "Cocos (Keeling) Islands"),
                Map.entry("CO", "Colombia"),
                Map.entry("KM", "Comoros"),
                Map.entry("CG", "Congo"),
                Map.entry("CD", "Congo, Democratic Republic"),
                Map.entry("CK", "Cook Islands"),
                Map.entry("CR", "Costa Rica"),
                Map.entry("CI", "Cote D'Ivoire"),
                Map.entry("HR", "Croatia"),
                Map.entry("CU", "Cuba"),
                Map.entry("CY", "Cyprus"),
                Map.entry("CZ", "Czech Republic"),
                Map.entry("DK", "Denmark"),
                Map.entry("DJ", "Djibouti"),
                Map.entry("DM", "Dominica"),
                Map.entry("DO", "Dominican Republic"),
                Map.entry("EC", "Ecuador"),
                Map.entry("EG", "Egypt"),
                Map.entry("SV", "El Salvador"),
                Map.entry("GQ", "Equatorial Guinea"),
                Map.entry("ER", "Eritrea"),
                Map.entry("EE", "Estonia"),
                Map.entry("ET", "Ethiopia"),
                Map.entry("FK", "Falkland Islands (Malvinas)"),
                Map.entry("FO", "Faroe Islands"),
                Map.entry("FJ", "Fiji"),
                Map.entry("FI", "Finland"),
                Map.entry("FR", "France"),
                Map.entry("GF", "French Guiana"),
                Map.entry("PF", "French Polynesia"),
                Map.entry("TF", "French Southern Territories"),
                Map.entry("GA", "Gabon"),
                Map.entry("GM", "Gambia"),
                Map.entry("GE", "Georgia"),
                Map.entry("DE", "Germany"),
                Map.entry("GH", "Ghana"),
                Map.entry("GI", "Gibraltar"),
                Map.entry("GR", "Greece"),
                Map.entry("GL", "Greenland"),
                Map.entry("GD", "Grenada"),
                Map.entry("GP", "Guadeloupe"),
                Map.entry("GU", "Guam"),
                Map.entry("GT", "Guatemala"),
                Map.entry("GG", "Guernsey"),
                Map.entry("GN", "Guinea"),
                Map.entry("GW", "Guinea-Bissau"),
                Map.entry("GY", "Guyana"),
                Map.entry("HT", "Haiti"),
                Map.entry("HM", "Heard Island & Mcdonald Islands"),
                Map.entry("VA", "Holy See (Vatican City State)"),
                Map.entry("HN", "Honduras"),
                Map.entry("HK", "Hong Kong"),
                Map.entry("HU", "Hungary"),
                Map.entry("IS", "Iceland"),
                Map.entry("IN", "India"),
                Map.entry("ID", "Indonesia"),
                Map.entry("IR", "Iran, Islamic Republic Of"),
                Map.entry("IQ", "Iraq"),
                Map.entry("IE", "Ireland"),
                Map.entry("IM", "Isle Of Man"),
                Map.entry("IL", "Israel"),
                Map.entry("IT", "Italy")

        );

        public static boolean isValidCode(String  code)
        {
            if(code != null)
            {
                return COUNTRY_MAP.containsKey(code.toUpperCase());
            }
            return false;
        }


    }

}
