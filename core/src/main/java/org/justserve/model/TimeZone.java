package org.justserve.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.micronaut.serde.annotation.Serdeable;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Serdeable
public enum TimeZone {
    INTERNATIONAL_DATE_LINE_WEST(1, "(UTC-12:00) International Date Line West", "Dateline Standard Time"),
    COORDINATED_UNIVERSAL_TIME_11(2, "(UTC-11:00) Coordinated Universal Time-11", "UTC-11"),
    ALEUTIAN_ISLANDS(3, "(UTC-10:00) Aleutian Islands", "Aleutian Standard Time"),
    HAWAII(4, "(UTC-10:00) Hawaii", "Hawaiian Standard Time"),
    MARQUESAS_ISLANDS(5, "(UTC-09:30) Marquesas Islands", "Marquesas Standard Time"),
    ALASKA(6, "(UTC-09:00) Alaska", "Alaskan Standard Time"),
    COORDINATED_UNIVERSAL_TIME_09(7, "(UTC-09:00) Coordinated Universal Time-09", "UTC-09"),
    BAJA_CALIFORNIA(8, "(UTC-08:00) Baja California", "Pacific Standard Time (Mexico)"),
    COORDINATED_UNIVERSAL_TIME_08(9, "(UTC-08:00) Coordinated Universal Time-08", "UTC-08"),
    PACIFIC_TIME_US_AND_CANADA(10, "(UTC-08:00) Pacific Time (US & Canada)", "Pacific Standard Time"),
    ARIZONA(11, "(UTC-07:00) Arizona", "US Mountain Standard Time"),
    LA_PAZ(12, "(UTC-07:00) La Paz", "Mountain Standard Time (Mexico)"),
    MOUNTAIN_TIME_US_AND_CANADA(13, "(UTC-07:00) Mountain Time (US & Canada)", "Mountain Standard Time"),
    YUKON(14, "(UTC-07:00) Yukon", "Yukon Standard Time"),
    CENTRAL_AMERICA(15, "(UTC-06:00) Central America", "Central America Standard Time"),
    CENTRAL_TIME_US_AND_CANADA(16, "(UTC-06:00) Central Time (US & Canada)", "Central Standard Time"),
    EASTER_ISLAND(17, "(UTC-06:00) Easter Island", "Easter Island Standard Time"),
    GUADALAJARA(18, "(UTC-06:00) Guadalajara", "Central Standard Time (Mexico)"),
    SASKATCHEWAN(19, "(UTC-06:00) Saskatchewan", "Canada Central Standard Time"),
    BOGOTA(20, "(UTC-05:00) Bogota", "SA Pacific Standard Time"),
    CHETUMAL(21, "(UTC-05:00) Chetumal", "Eastern Standard Time (Mexico)"),
    EASTERN_TIME_US_AND_CANADA(22, "(UTC-05:00) Eastern Time (US & Canada)", "Eastern Standard Time"),
    HAITI(23, "(UTC-05:00) Haiti", "Haiti Standard Time"),
    HAVANA(24, "(UTC-05:00) Havana", "Cuba Standard Time"),
    INDIANA_EAST(25, "(UTC-05:00) Indiana (East)", "US Eastern Standard Time"),
    TURKS_AND_CAICOS(26, "(UTC-05:00) Turks and Caicos", "Turks And Caicos Standard Time"),
    ASUNCION(27, "(UTC-04:00) Asuncion", "Paraguay Standard Time"),
    ATLANTIC_TIME_CANADA(28, "(UTC-04:00) Atlantic Time (Canada)", "Atlantic Standard Time"),
    CARACAS(29, "Caracas", "Venezuela Standard Time"),
    CUIABA(30, "(UTC-04:00) Cuiaba", "Central Brazilian Standard Time"),
    GEORGETOWN(31, "(UTC-04:00) Georgetown", "SA Western Standard Time"),
    SANTIAGO(32, "(UTC-04:00) Santiago", "Pacific SA Standard Time"),
    NEWFOUNDLAND(33, "(UTC-03:30) Newfoundland", "Newfoundland Standard Time"),
    ARAGUAINA(34, "(UTC-03:00) Araguaina", "Tocantins Standard Time"),
    BRASILIA(35, "(UTC-03:00) Brasilia", "E. South America Standard Time"),
    CAYENNE(36, "(UTC-03:00) Cayenne", "SA Eastern Standard Time"),
    CITY_OF_BUENOS_AIRES(37, "(UTC-03:00) City of Buenos Aires", "Argentina Standard Time"),
    MONTEVIDEO(38, "(UTC-03:00) Montevideo", "Montevideo Standard Time"),
    PUNTA_ARENAS(39, "(UTC-03:00) Punta Arenas", "Magallanes Standard Time"),
    SAINT_PIERRE_AND_MIQUELON(40, "(UTC-03:00) Saint Pierre and Miquelon", "Saint Pierre Standard Time"),
    SALVADOR(41, "(UTC-03:00) Salvador", "Bahia Standard Time"),
    COORDINATED_UNIVERSAL_TIME_02(42, "(UTC-02:00) Coordinated Universal Time-02", "UTC-02"),
    GREENLAND(43, "(UTC-02:00) Greenland", "Greenland Standard Time"),
    MID_ATLANTIC_OLD(44, "(UTC-02:00) Mid-Atlantic - Old", "Mid-Atlantic Standard Time"),
    AZORES(45, "(UTC-01:00) Azores", "Azores Standard Time"),
    CABO_VERDE_IS(46, "(UTC-01:00) Cabo Verde Is.", "Cape Verde Standard Time"),
    COORDINATED_UNIVERSAL_TIME(47, "(UTC) Coordinated Universal Time", "UTC"),
    DUBLIN(48, "(UTC+00:00) Dublin", "GMT Standard Time"),
    MONROVIA(49, "(UTC+00:00) Monrovia", "Greenwich Standard Time"),
    SAO_TOME(50, "(UTC+00:00) Sao Tome", "Sao Tome Standard Time"),
    CASABLANCA(51, "(UTC+01:00) Casablanca", "Morocco Standard Time"),
    AMSTERDAM(52, "(UTC+01:00) Amsterdam", "W. Europe Standard Time"),
    BELGRADE(53, "(UTC+01:00) Belgrade", "Central Europe Standard Time"),
    BRUSSELS(54, "(UTC+01:00) Brussels", "Romance Standard Time"),
    SARAJEVO(55, "(UTC+01:00) Sarajevo", "Central European Standard Time"),
    WEST_CENTRAL_AFRICA(56, "(UTC+01:00) West Central Africa", "W. Central Africa Standard Time"),
    ATHENS(57, "(UTC+02:00) Athens", "GTB Standard Time"),
    BEIRUT(58, "(UTC+02:00) Beirut", "Middle East Standard Time"),
    CAIRO(59, "(UTC+02:00) Cairo", "Egypt Standard Time"),
    CHISINAU(60, "(UTC+02:00) Chisinau", "E. Europe Standard Time"),
    GAZA(61, "(UTC+02:00) Gaza", "West Bank Standard Time"),
    HARARE(62, "(UTC+02:00) Harare", "South Africa Standard Time"),
    HELSINKI(63, "(UTC+02:00) Helsinki", "FLE Standard Time"),
    JERUSALEM(64, "(UTC+02:00) Jerusalem", "Israel Standard Time"),
    JUBA(65, "(UTC+02:00) Juba", "South Sudan Standard Time"),
    KALININGRAD(66, "(UTC+02:00) Kaliningrad", "Kaliningrad Standard Time"),
    KHARTOUM(67, "(UTC+02:00) Khartoum", "Sudan Standard Time"),
    TRIPOLI(68, "(UTC+02:00) Tripoli", "Libya Standard Time"),
    WINDHOEK(69, "(UTC+02:00) Windhoek", "Namibia Standard Time"),
    AMMAN(70, "(UTC+03:00) Amman", "Jordan Standard Time"),
    BAGHDAD(71, "(UTC+03:00) Baghdad", "Arabic Standard Time"),
    DAMASCUS(72, "(UTC+03:00) Damascus", "Syria Standard Time"),
    ISTANBUL(73, "(UTC+03:00) Istanbul", "Turkey Standard Time"),
    KUWAIT(74, "(UTC+03:00) Kuwait", "Arab Standard Time"),
    MINSK(75, "(UTC+03:00) Minsk", "Belarus Standard Time"),
    MOSCOW(76, "(UTC+03:00) Moscow", "Russian Standard Time"),
    NAIROBI(77, "(UTC+03:00) Nairobi", "E. Africa Standard Time"),
    VOLGOGRAD(78, "(UTC+03:00) Volgograd", "Volgograd Standard Time"),
    TEHRAN(79, "(UTC+03:30) Tehran", "Iran Standard Time"),
    ABU_DHABI(80, "(UTC+04:00) Abu Dhabi", "Arabian Standard Time"),
    ASTRAKHAN(81, "(UTC+04:00) Astrakhan", "Astrakhan Standard Time"),
    BAKU(82, "(UTC+04:00) Baku", "Azerbaijan Standard Time"),
    IZHEVSK(83, "(UTC+04:00) Izhevsk", "Russia Time Zone 3"),
    PORT_LOUIS(84, "(UTC+04:00) Port Louis", "Mauritius Standard Time"),
    SARATOV(85, "(UTC+04:00) Saratov", "Samara Time"),
    TBILISI(86, "(UTC+04:00) Tbilisi", "Georgian Standard Time"),
    YEREVAN(87, "(UTC+04:00) Yerevan", "Caucasus Standard Time"),
    KABUL(88, "(UTC+04:30) Kabul", "Afghanistan Standard Time"),
    ASHGABAT(89, "(UTC+05:00) Ashgabat", "West Asia Standard Time"),
    ASTANA(90, "(UTC+05:00) Astana", "Central Asia Standard Time"),
    EKATERINBURG(91, "(UTC+05:00) Ekaterinburg", "Ekaterinburg Standard Time"),
    ISLAMABAD(92, "(UTC+05:00) Islamabad", "Pakistan Standard Time"),
    CHENNAI(93, "(UTC+05:30) Chennai", "India Standard Time"),
    SRI_JAYAWARDENEPURA(94, "(UTC+05:30) Sri Jayawardenepura", "Sri Lanka Standard Time"),
    KATHMANDU(95, "(UTC+05:45) Kathmandu", "Nepal Standard Time"),
    BISHKEK(96, "(UTC+06:00) Bishkek", "Central Asia Standard Time"),
    DHAKA(97, "(UTC+06:00) Dhaka", "Bangladesh Standard Time"),
    OMSK(98, "(UTC+06:00) Omsk", "Omsk Standard Time"),
    YANGON_RANGOON(99, "(UTC+06:30) Yangon (Rangoon)", "Myanmar Standard Time"),
    BANGKOK(100, "(UTC+07:00) Bangkok", "SE Asia Standard Time"),
    BARNAUL(101, "(UTC+07:00) Barnaul", "Altai Standard Time"),
    HOVD(102, "(UTC+07:00) Hovd", "W. Mongolia Standard Time"),
    KRASNOYARSK(103, "(UTC+07:00) Krasnoyarsk", "North Asia Standard Time"),
    NOVOSIBIRSK(104, "(UTC+07:00) Novosibirsk", "N. Central Asia Standard Time"),
    TOMSK(105, "(UTC+07:00) Tomsk", "Tomsk Standard Time"),
    BEIJING(106, "(UTC+08:00) Beijing", "China Standard Time"),
    IRKUTSK(107, "(UTC+08:00) Irkutsk", "North Asia East Standard Time"),
    KUALA_LUMPUR(108, "(UTC+08:00) Kuala Lumpur", "Singapore Standard Time"),
    PERTH(109, "(UTC+08:00) Perth", "W. Australia Standard Time"),
    TAIPEI(110, "(UTC+08:00) Taipei", "Taipei Standard Time"),
    ULAANBAATAR(111, "(UTC+08:00) Ulaanbaatar", "Ulaanbaatar Standard Time"),
    EUCLA(112, "(UTC+08:45) Eucla", "Aus Central W. Standard Time"),
    CHITA(113, "(UTC+09:00) Chita", "Transbaikal Standard Time"),
    OSAKA(114, "(UTC+09:00) Osaka", "Tokyo Standard Time"),
    PYONGYANG(115, "(UTC+09:00) Pyongyang", "North Korea Standard Time"),
    SEOUL(116, "(UTC+09:00) Seoul", "Korea Standard Time"),
    YAKUTSK(117, "(UTC+09:00) Yakutsk", "Yakutsk Standard Time"),
    ADELAIDE(118, "(UTC+09:30) Adelaide", "Cen. Australia Standard Time"),
    DARWIN(119, "(UTC+09:30) Darwin", "AUS Central Standard Time"),
    BRISBANE(120, "(UTC+10:00) Brisbane", "E. Australia Standard Time"),
    CANBERRA(121, "(UTC+10:00) Canberra", "AUS Eastern Standard Time"),
    GUAM(122, "(UTC+10:00) Guam", "West Pacific Standard Time"),
    HOBART(123, "(UTC+10:00) Hobart", "Tasmania Standard Time"),
    VLADIVOSTOK(124, "(UTC+10:00) Vladivostok", "Vladivostok Standard Time"),
    LORD_HOWE_ISLAND(125, "(UTC+10:30) Lord Howe Island", "Lord Howe Standard Time"),
    BOUGAINVILLE_ISLAND(126, "(UTC+11:00) Bougainville Island", "Bougainville Standard Time"),
    CHOKURDAKH(127, "(UTC+11:00) Chokurdakh", "Russia Time Zone 10"),
    MAGADAN(128, "(UTC+11:00) Magadan", "Magadan Standard Time"),
    NORFOLK_ISLAND(129, "(UTC+11:00) Norfolk Island", "Norfolk Standard Time"),
    SAKHALIN(130, "(UTC+11:00) Sakhalin", "Sakhalin Standard Time"),
    SOLOMON_IS(131, "(UTC+11:00) Solomon Is.", "Central Pacific Standard Time"),
    ANADYR(132, "(UTC+12:00) Anadyr", "Russia Time Zone 11"),
    AUCKLAND(133, "(UTC+12:00) Auckland", "New Zealand Standard Time"),
    COORDINATED_UNIVERSAL_TIME_12(134, "(UTC+12:00) Coordinated Universal Time+12", "UTC+12"),
    FIJI(135, "(UTC+12:00) Fiji", "Fiji Standard Time"),
    PETROPAVLOVSK_KAMCHATSKY_OLD(136, "(UTC+12:00) Petropavlovsk-Kamchatsky - Old", "Kamchatka Standard Time"),
    CHATHAM_ISLANDS(137, "(UTC+12:45) Chatham Islands", "Chatham Islands Standard Time"),
    COORDINATED_UNIVERSAL_TIME_13(138, "(UTC+13:00) Coordinated Universal Time+13", "UTC+13"),
    NUKU_ALOFA(139, "(UTC+13:00) Nuku'alofa", "Tonga Standard Time"),
    SAMOA(140, "(UTC+13:00) Samoa", "Samoa Standard Time"),
    KIRITIMATI_ISLAND(141, "(UTC+14:00) Kiritimati Island", "Line Islands Standard Time");

    public static final Map<Integer, TimeZone> VALUE_MAPPING = Map.copyOf(Arrays.stream(values())
            .collect(Collectors.toMap(v -> v.intValue, Function.identity())));

    private final Integer intValue;
    private final String queryValue;
    private final String responseValue;

    @Override
    @JsonValue
    public String toString() {
        return queryValue;
    }


    @JsonCreator
    public static TimeZone fromValue(Object value) {
        if (value instanceof Number) {
            int intVal = ((Number) value).intValue();
            for (TimeZone type : values()) {
                if (type.intValue == intVal) return type;
            }
        } else if (value instanceof String strVal) {
            for (TimeZone type : values()) {
                if (type.queryValue.equalsIgnoreCase(strVal) || type.responseValue.equalsIgnoreCase(strVal)) {
                    return type;
                }
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "' for TimeZone");
    }
}
