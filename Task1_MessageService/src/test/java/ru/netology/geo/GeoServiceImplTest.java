package ru.netology.geo;

import org.junit.Test;

import ru.netology.entity.Country;
import ru.netology.entity.Location;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Класс для тестирования GeoServiceImpl
 * Проверяет корректность определения локации по IP-адресу
 */
public class GeoServiceImplTest {

    // Создаем экземпляр тестируемого класса
    private final GeoServiceImpl geoService = new GeoServiceImpl();

    /**
     * Тест проверяет определение локации для localhost
     * Ожидается, что вернется локация с null значениями
     */
    @Test
    public void testByIpForLocalhost() {
        // Выполнение тестируемого метода
        Location location = geoService.byIp(GeoServiceImpl.LOCALHOST);

        // Проверка результата
        // Для localhost должна вернуться локация с null значениями
        assertNull(location.getCity());
        assertNull(location.getCountry());
        assertNull(location.getStreet());
        assertEquals(0, location.getBuiling());
    }

    /**
     * Тест проверяет определение локации для конкретного московского IP
     * Ожидается, что вернется локация с данными Москвы
     */
    @Test
    public void testByIpForMoscowIp() {
        // Выполнение тестируемого метода
        Location location = geoService.byIp(GeoServiceImpl.MOSCOW_IP);

        // Проверка результата
        // Для московского IP должна вернуться локация с данными Москвы
        assertEquals("Moscow", location.getCity());
        assertEquals(Country.RUSSIA, location.getCountry());
        assertEquals("Lenina", location.getStreet());
        assertEquals(15, location.getBuiling());
    }

    /**
     * Тест проверяет определение локации для конкретного нью-йоркского IP
     * Ожидается, что вернется локация с данными Нью-Йорка
     */
    @Test
    public void testByIpForNewYorkIp() {
        // Выполнение тестируемого метода
        Location location = geoService.byIp(GeoServiceImpl.NEW_YORK_IP);

        // Проверка результата
        // Для нью-йоркского IP должна вернуться локация с данными Нью-Йорка
        assertEquals("New York", location.getCity());
        assertEquals(Country.USA, location.getCountry());
        assertEquals(" 10th Avenue", location.getStreet());
        assertEquals(32, location.getBuiling());
    }

    /**
     * Тест проверяет определение локации для IP из российского сегмента (начинается с "172.")
     * Ожидается, что вернется локация с данными России
     */
    @Test
    public void testByIpForRussianIpSegment() {
        // Подготовка данных для теста
        // Используем IP-адрес из российского сегмента
        String russianIp = "172.16.0.1";

        // Выполнение тестируемого метода
        Location location = geoService.byIp(russianIp);

        // Проверка результата
        // Для IP из российского сегмента должна вернуться локация с данными России
        assertEquals("Moscow", location.getCity());
        assertEquals(Country.RUSSIA, location.getCountry());
        assertNull(location.getStreet());
        assertEquals(0, location.getBuiling());
    }

    /**
     * Тест проверяет определение локации для IP из американского сегмента (начинается с "96.")
     * Ожидается, что вернется локация с данными США
     */
    @Test
    public void testByIpForAmericanIpSegment() {
        // Подготовка данных для теста
        // Используем IP-адрес из американского сегмента
        String americanIp = "96.1.1.1";

        // Выполнение тестируемого метода
        Location location = geoService.byIp(americanIp);

        // Проверка результата
        // Для IP из американского сегмента должна вернуться локация с данными США
        assertEquals("New York", location.getCity());
        assertEquals(Country.USA, location.getCountry());
        assertNull(location.getStreet());
        assertEquals(0, location.getBuiling());
    }

    /**
     * Тест проверяет определение локации для неизвестного IP-адреса
     * Ожидается, что вернется null
     */
    @Test
    public void testByIpForUnknownIp() {
        // Подготовка данных для теста
        // Используем неизвестный IP-адрес
        String unknownIp = "192.168.1.1";

        // Выполнение тестируемого метода
        Location location = geoService.byIp(unknownIp);

        // Проверка результата
        // Для неизвестного IP должна вернуться null
        assertNull(location);
    }
}
