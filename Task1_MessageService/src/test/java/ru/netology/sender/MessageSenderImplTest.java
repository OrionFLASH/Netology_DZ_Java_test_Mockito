package ru.netology.sender;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Класс для тестирования MessageSenderImpl
 * Проверяет корректность отправки сообщений на разных языках
 * в зависимости от IP-адреса пользователя
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageSenderImplTest {

    // Создаем мок-объект для GeoService
    // Этот объект будет использоваться для имитации определения локации по IP
    @Mock
    private GeoService geoService;

    // Создаем мок-объект для LocalizationService
    // Этот объект будет использоваться для имитации получения локализованного текста
    @Mock
    private LocalizationService localizationService;

    // Инъекция моков в тестируемый объект
    // MessageSenderImpl будет использовать наши мок-объекты вместо реальных
    @InjectMocks
    private MessageSenderImpl messageSender;

    /**
     * Тест проверяет, что для российского IP-адреса (начинающегося с "172.")
     * всегда отправляется русский текст
     */
    @Test
    public void testSendRussianTextForRussianIp() {
        // Подготовка данных для теста
        // Создаем заголовки с российским IP-адресом
        Map<String, String> headers = new HashMap<>();
        String russianIp = "172.0.32.11";
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, russianIp);

        // Создаем локацию для России
        Location russianLocation = new Location("Moscow", Country.RUSSIA, "Lenina", 15);

        // Настраиваем поведение мок-объектов
        // Когда geoService.byIp вызывается с российским IP, возвращаем российскую локацию
        when(geoService.byIp(russianIp)).thenReturn(russianLocation);
        // Когда localizationService.locale вызывается с Country.RUSSIA, возвращаем русский текст
        when(localizationService.locale(Country.RUSSIA)).thenReturn("Добро пожаловать");

        // Выполнение тестируемого метода
        String result = messageSender.send(headers);

        // Проверка результата
        // Ожидаем, что вернется русский текст
        assertEquals("Добро пожаловать", result);
    }

    /**
     * Тест проверяет, что для американского IP-адреса (начинающегося с "96.")
     * всегда отправляется английский текст
     */
    @Test
    public void testSendEnglishTextForAmericanIp() {
        // Подготовка данных для теста
        // Создаем заголовки с американским IP-адресом
        Map<String, String> headers = new HashMap<>();
        String americanIp = "96.44.183.149";
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, americanIp);

        // Создаем локацию для США
        Location americanLocation = new Location("New York", Country.USA, "10th Avenue", 32);

        // Настраиваем поведение мок-объектов
        // Когда geoService.byIp вызывается с американским IP, возвращаем американскую локацию
        when(geoService.byIp(americanIp)).thenReturn(americanLocation);
        // Когда localizationService.locale вызывается с Country.USA, возвращаем английский текст
        when(localizationService.locale(Country.USA)).thenReturn("Welcome");

        // Выполнение тестируемого метода
        String result = messageSender.send(headers);

        // Проверка результата
        // Ожидаем, что вернется английский текст
        assertEquals("Welcome", result);
    }

    /**
     * Тест проверяет поведение при отсутствии IP-адреса в заголовках
     * В этом случае должен возвращаться текст для США (по умолчанию)
     */
    @Test
    public void testSendDefaultTextWhenIpIsMissing() {
        // Подготовка данных для теста
        // Создаем заголовки без IP-адреса
        Map<String, String> headers = new HashMap<>();

        // Настраиваем поведение мок-объекта
        // Когда localizationService.locale вызывается с Country.USA, возвращаем английский текст
        when(localizationService.locale(Country.USA)).thenReturn("Welcome");

        // Выполнение тестируемого метода
        String result = messageSender.send(headers);

        // Проверка результата
        // Ожидаем, что вернется английский текст по умолчанию
        assertEquals("Welcome", result);
    }

    /**
     * Тест проверяет поведение для IP-адреса, начинающегося с "172."
     * (российский сегмент, но не конкретный адрес)
     */
    @Test
    public void testSendRussianTextForRussianIpSegment() {
        // Подготовка данных для теста
        // Создаем заголовки с IP-адресом из российского сегмента
        Map<String, String> headers = new HashMap<>();
        String russianIpSegment = "172.16.0.1";
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, russianIpSegment);

        // Создаем локацию для России
        Location russianLocation = new Location("Moscow", Country.RUSSIA, null, 0);

        // Настраиваем поведение мок-объектов
        // Когда geoService.byIp вызывается с IP из российского сегмента, возвращаем российскую локацию
        when(geoService.byIp(russianIpSegment)).thenReturn(russianLocation);
        // Когда localizationService.locale вызывается с Country.RUSSIA, возвращаем русский текст
        when(localizationService.locale(Country.RUSSIA)).thenReturn("Добро пожаловать");

        // Выполнение тестируемого метода
        String result = messageSender.send(headers);

        // Проверка результата
        // Ожидаем, что вернется русский текст
        assertEquals("Добро пожаловать", result);
    }

    /**
     * Тест проверяет поведение для IP-адреса, начинающегося с "96."
     * (американский сегмент, но не конкретный адрес)
     */
    @Test
    public void testSendEnglishTextForAmericanIpSegment() {
        // Подготовка данных для теста
        // Создаем заголовки с IP-адресом из американского сегмента
        Map<String, String> headers = new HashMap<>();
        String americanIpSegment = "96.1.1.1";
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, americanIpSegment);

        // Создаем локацию для США
        Location americanLocation = new Location("New York", Country.USA, null, 0);

        // Настраиваем поведение мок-объектов
        // Когда geoService.byIp вызывается с IP из американского сегмента, возвращаем американскую локацию
        when(geoService.byIp(americanIpSegment)).thenReturn(americanLocation);
        // Когда localizationService.locale вызывается с Country.USA, возвращаем английский текст
        when(localizationService.locale(Country.USA)).thenReturn("Welcome");

        // Выполнение тестируемого метода
        String result = messageSender.send(headers);

        // Проверка результата
        // Ожидаем, что вернется английский текст
        assertEquals("Welcome", result);
    }
}
