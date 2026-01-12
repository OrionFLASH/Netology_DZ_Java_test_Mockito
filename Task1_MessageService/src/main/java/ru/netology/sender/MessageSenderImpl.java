package ru.netology.sender;

import java.util.Map;

import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;

public class MessageSenderImpl implements MessageSender {

    public static final String IP_ADDRESS_HEADER = "x-real-ip";
    private final GeoService geoService;

    private final LocalizationService localizationService;

    public MessageSenderImpl(GeoService geoService, LocalizationService localizationService) {
        this.geoService = geoService;
        this.localizationService = localizationService;
    }

    public String send(Map<String, String> headers) {
        String ipAddress = String.valueOf(headers.get(IP_ADDRESS_HEADER));
        // Проверяем, что IP-адрес не null и не пустой, а также не равен строке "null"
        if (ipAddress != null && !ipAddress.isEmpty() && !"null".equals(ipAddress)) {
            Location location = geoService.byIp(ipAddress);
            // Проверяем, что локация определена и страна не null
            if (location != null && location.getCountry() != null) {
                System.out.printf("Отправлено сообщение: %s", localizationService.locale(location.getCountry()));
                return localizationService.locale(location.getCountry());
            }
        }
        // Возвращаем текст по умолчанию для США, если IP не определен или локация не найдена
        return localizationService.locale(Country.USA);
    }
}
