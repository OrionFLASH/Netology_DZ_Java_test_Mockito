package ru.netology.i18n;

import org.junit.Test;

import ru.netology.entity.Country;

import static org.junit.Assert.assertEquals;

/**
 * Класс для тестирования LocalizationServiceImpl
 * Проверяет корректность возврата локализованного текста
 * в зависимости от страны
 */
public class LocalizationServiceImplTest {

    // Создаем экземпляр тестируемого класса
    private final LocalizationServiceImpl localizationService = new LocalizationServiceImpl();

    /**
     * Тест проверяет возврат русского текста для России
     * Ожидается, что вернется "Добро пожаловать"
     */
    @Test
    public void testLocaleForRussia() {
        // Выполнение тестируемого метода
        String result = localizationService.locale(Country.RUSSIA);

        // Проверка результата
        // Для России должен вернуться русский текст
        assertEquals("Добро пожаловать", result);
    }

    /**
     * Тест проверяет возврат английского текста для США
     * Ожидается, что вернется "Welcome"
     */
    @Test
    public void testLocaleForUSA() {
        // Выполнение тестируемого метода
        String result = localizationService.locale(Country.USA);

        // Проверка результата
        // Для США должен вернуться английский текст
        assertEquals("Welcome", result);
    }

    /**
     * Тест проверяет возврат английского текста для Германии
     * Ожидается, что вернется "Welcome" (по умолчанию для всех стран кроме России)
     */
    @Test
    public void testLocaleForGermany() {
        // Выполнение тестируемого метода
        String result = localizationService.locale(Country.GERMANY);

        // Проверка результата
        // Для Германии должен вернуться английский текст (по умолчанию)
        assertEquals("Welcome", result);
    }

    /**
     * Тест проверяет возврат английского текста для Бразилии
     * Ожидается, что вернется "Welcome" (по умолчанию для всех стран кроме России)
     */
    @Test
    public void testLocaleForBrazil() {
        // Выполнение тестируемого метода
        String result = localizationService.locale(Country.BRAZIL);

        // Проверка результата
        // Для Бразилии должен вернуться английский текст (по умолчанию)
        assertEquals("Welcome", result);
    }
}
