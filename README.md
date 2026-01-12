# Домашнее задание: Тестирование с использованием Mockito

## Описание проекта

Данный проект содержит решения двух задач по тестированию Java-приложений с использованием библиотеки Mockito. Каждая задача реализована в отдельной подпапке и может быть запущена независимо от другой.

## Структура проекта

```
Netology_DZ_Java_test_Mockito/
├── Task1_MessageService/          # Задача 1: Тестирование сервиса отправки сообщений
│   ├── src/
│   │   ├── main/java/             # Исходный код приложения
│   │   └── test/java/              # Тесты
│   └── pom.xml                     # Конфигурация Maven
├── Task2_MedicalService/           # Задача 2: Тестирование сервиса медицинских показаний
│   ├── src/
│   │   ├── main/java/             # Исходный код приложения
│   │   └── test/java/              # Тесты
│   └── pom.xml                     # Конфигурация Maven
└── Docs/                           # Исходные задания
    ├── Task1_MessageService_README.md
    └── Task2_MedicalService_README.md
```

## Задача 1: Тестирование сервиса отправки сообщений

### Описание задачи

Необходимо протестировать сервис отправки локализованных сообщений, который определяет язык сообщения на основе IP-адреса пользователя:
- IP-адреса, начинающиеся с "172.", относятся к российскому сегменту
- IP-адреса, начинающиеся с "96.", относятся к американскому сегменту
- Для российских адресов отправляется текст на русском языке
- Для американских адресов и всех остальных - на английском

### Реализованные тесты

#### 1. MessageSenderImplTest

Класс тестирует `MessageSenderImpl` с использованием мок-объектов для `GeoService` и `LocalizationService`.

**Тесты:**
- `testSendRussianTextForRussianIp()` - проверяет отправку русского текста для российского IP
- `testSendEnglishTextForAmericanIp()` - проверяет отправку английского текста для американского IP
- `testSendDefaultTextWhenIpIsMissing()` - проверяет поведение при отсутствии IP-адреса
- `testSendRussianTextForRussianIpSegment()` - проверяет отправку русского текста для IP из российского сегмента
- `testSendEnglishTextForAmericanIpSegment()` - проверяет отправку английского текста для IP из американского сегмента

**Использованные техники:**
- `@Mock` - создание мок-объектов для зависимостей
- `@InjectMocks` - автоматическая инъекция моков в тестируемый объект
- `when().thenReturn()` - настройка поведения мок-объектов

#### 2. GeoServiceImplTest

Класс тестирует `GeoServiceImpl` для проверки корректности определения локации по IP-адресу.

**Тесты:**
- `testByIpForLocalhost()` - проверяет определение локации для localhost
- `testByIpForMoscowIp()` - проверяет определение локации для московского IP
- `testByIpForNewYorkIp()` - проверяет определение локации для нью-йоркского IP
- `testByIpForRussianIpSegment()` - проверяет определение локации для IP из российского сегмента
- `testByIpForAmericanIpSegment()` - проверяет определение локации для IP из американского сегмента
- `testByIpForUnknownIp()` - проверяет поведение для неизвестного IP

#### 3. LocalizationServiceImplTest

Класс тестирует `LocalizationServiceImpl` для проверки корректности возврата локализованного текста.

**Тесты:**
- `testLocaleForRussia()` - проверяет возврат русского текста для России
- `testLocaleForUSA()` - проверяет возврат английского текста для США
- `testLocaleForGermany()` - проверяет возврат английского текста для Германии (по умолчанию)
- `testLocaleForBrazil()` - проверяет возврат английского текста для Бразилии (по умолчанию)

### Исправления в исходном коде

В классе `MessageSenderImpl` добавлена проверка на `null` для предотвращения `NullPointerException`:
- Проверка, что IP-адрес не равен строке "null"
- Проверка, что локация определена и страна не null

### Запуск тестов

```bash
cd Task1_MessageService
mvn test
```

**Результат:** Все 15 тестов проходят успешно.

---

## Задача 2: Тестирование сервиса медицинских показаний

### Описание задачи

Необходимо протестировать сервис обработки медицинских показаний пациентов (кровяное давление, температура) с использованием мок-объектов для зависимостей.

### Реализованные тесты

#### MedicalServiceImplTest

Класс тестирует `MedicalServiceImpl` с использованием мок-объектов для `PatientInfoRepository` и `SendAlertService`.

**Тесты:**
- `testCheckBloodPressureWhenPressureIsAbnormal()` - проверяет вывод сообщения при отклонении давления от нормы
- `testCheckBloodPressureWhenPressureIsNormal()` - проверяет, что сообщение не выводится, когда давление в норме
- `testCheckTemperatureWhenTemperatureIsAbnormal()` - проверяет вывод сообщения при отклонении температуры от нормы
- `testCheckTemperatureWhenTemperatureIsNormal()` - проверяет, что сообщение не выводится, когда температура в норме
- `testCheckTemperatureWhenTemperatureIsHigh()` - проверяет поведение при высокой температуре

**Использованные техники:**
- `@Mock` - создание мок-объектов для зависимостей
- `@InjectMocks` - автоматическая инъекция моков в тестируемый объект
- `ArgumentCaptor` - захват аргументов, переданных в метод мок-объекта
- `verify()` - проверка вызова методов мок-объектов
- `times()` и `never()` - проверка количества вызовов методов

### Исправления в исходном коде

В классе `MedicalServiceImpl` исправлена синтаксическая ошибка в методе `checkTemperature()`:
- Разделены два вызова, которые были написаны в одной строке

### Запуск тестов

```bash
cd Task2_MedicalService
mvn test
```

**Результат:** Все 5 тестов проходят успешно.

---

## Технологии и инструменты

- **Java 11** - язык программирования
- **Maven** - система сборки проектов
- **JUnit 4.13.2** - фреймворк для написания тестов
- **Mockito 3.12.4** - библиотека для создания мок-объектов

## Основные концепции Mockito, использованные в проекте

### 1. Создание мок-объектов

```java
@Mock
private GeoService geoService;
```

### 2. Настройка поведения моков

```java
when(geoService.byIp("172.0.32.11")).thenReturn(russianLocation);
```

### 3. Проверка вызовов методов

```java
verify(alertService, times(1)).send(messageCaptor.capture());
```

### 4. Захват аргументов

```java
ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
```

### 5. Инъекция моков

```java
@InjectMocks
private MessageSenderImpl messageSender;
```

## История версий

### Версия 1.0 (2026-01-13)

**Задача 1:**
- Создана структура проекта для тестирования сервиса отправки сообщений
- Добавлены зависимости JUnit и Mockito в pom.xml
- Написаны тесты для MessageSenderImpl (5 тестов)
- Написаны тесты для GeoServiceImpl (6 тестов)
- Написаны тесты для LocalizationServiceImpl (4 теста)
- Исправлена ошибка с проверкой null в MessageSenderImpl
- Все тесты проходят успешно (15 тестов)

**Задача 2:**
- Создана структура проекта для тестирования сервиса медицинских показаний
- Добавлены зависимости JUnit и Mockito в pom.xml
- Написаны тесты для MedicalServiceImpl (5 тестов)
- Исправлена синтаксическая ошибка в MedicalServiceImpl
- Все тесты проходят успешно (5 тестов)

**Общие изменения:**
- Создана структура проекта с двумя независимыми задачами
- Добавлена подробная документация
- Добавлены комментарии в код на русском языке
- Создан .gitignore
- Проект готов к размещению на GitHub

## Переменные и функции

### Task1_MessageService

#### Класс MessageSenderImpl

**Переменные:**
- `IP_ADDRESS_HEADER` (String) - константа с именем заголовка для IP-адреса
- `geoService` (GeoService) - сервис для определения локации по IP
- `localizationService` (LocalizationService) - сервис для получения локализованного текста

**Методы:**
- `send(Map<String, String> headers)` - отправляет сообщение на основе IP-адреса из заголовков

**Пример использования:**
```java
Map<String, String> headers = new HashMap<>();
headers.put("x-real-ip", "172.0.32.11");
String message = messageSender.send(headers); // Вернет "Добро пожаловать"
```

#### Класс GeoServiceImpl

**Методы:**
- `byIp(String ip)` - определяет локацию по IP-адресу

**Пример использования:**
```java
Location location = geoService.byIp("172.0.32.11");
// Вернет Location с данными Москвы
```

#### Класс LocalizationServiceImpl

**Методы:**
- `locale(Country country)` - возвращает локализованный текст для указанной страны

**Пример использования:**
```java
String text = localizationService.locale(Country.RUSSIA);
// Вернет "Добро пожаловать"
```

### Task2_MedicalService

#### Класс MedicalServiceImpl

**Переменные:**
- `patientInfoRepository` (PatientInfoRepository) - репозиторий для работы с данными пациентов
- `alertService` (SendAlertService) - сервис для отправки уведомлений

**Методы:**
- `checkBloodPressure(String patientId, BloodPressure bloodPressure)` - проверяет кровяное давление пациента
- `checkTemperature(String patientId, BigDecimal temperature)` - проверяет температуру пациента
- `getPatientInfo(String patientId)` - получает информацию о пациенте (приватный метод)

**Пример использования:**
```java
BloodPressure pressure = new BloodPressure(150, 100);
medicalService.checkBloodPressure("patient-123", pressure);
// Если давление отклоняется от нормы, будет отправлено уведомление
```

## Запуск проекта

### Предварительные требования

- Java 11 или выше
- Maven 3.6 или выше

### Запуск тестов для Task1

```bash
cd Task1_MessageService
mvn clean test
```

### Запуск тестов для Task2

```bash
cd Task2_MedicalService
mvn clean test
```

### Запуск всех тестов

```bash
cd Task1_MessageService && mvn test && cd ../Task2_MedicalService && mvn test
```

## Автор

Проект выполнен в рамках домашнего задания по курсу Java-разработки.

## Лицензия

Данный проект создан в учебных целях.
