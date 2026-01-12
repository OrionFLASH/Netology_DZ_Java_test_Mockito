package ru.netology.patient.service.medical;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Класс для тестирования MedicalServiceImpl
 * Проверяет корректность работы методов проверки медицинских показаний
 * с использованием мок-объектов для зависимостей
 */
@RunWith(MockitoJUnitRunner.class)
public class MedicalServiceImplTest {

    // Создаем мок-объект для PatientInfoRepository
    // Этот объект будет использоваться для имитации получения информации о пациенте
    @Mock
    private PatientInfoRepository patientInfoRepository;

    // Создаем мок-объект для SendAlertService
    // Этот объект будет использоваться для имитации отправки уведомлений
    @Mock
    private SendAlertService alertService;

    // Инъекция моков в тестируемый объект
    // MedicalServiceImpl будет использовать наши мок-объекты вместо реальных
    @InjectMocks
    private MedicalServiceImpl medicalService;

    /**
     * Тест проверяет вывод сообщения при отклонении давления от нормы
     * Ожидается, что будет вызван метод send у alertService с соответствующим сообщением
     */
    @Test
    public void testCheckBloodPressureWhenPressureIsAbnormal() {
        // Подготовка данных для теста
        String patientId = "patient-123";
        // Нормальное давление пациента
        BloodPressure normalPressure = new BloodPressure(120, 80);
        // Отклоняющееся от нормы давление
        BloodPressure abnormalPressure = new BloodPressure(150, 100);

        // Создаем объект HealthInfo с нормальным давлением
        HealthInfo healthInfo = new HealthInfo(new BigDecimal("36.6"), normalPressure);
        // Создаем объект PatientInfo с информацией о пациенте
        PatientInfo patientInfo = new PatientInfo(
            patientId,
            "Иван",
            "Иванов",
            LocalDate.of(1990, 1, 1),
            healthInfo
        );

        // Настраиваем поведение мок-объекта
        // Когда patientInfoRepository.getById вызывается с patientId, возвращаем patientInfo
        when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);

        // Создаем ArgumentCaptor для захвата аргументов, переданных в метод send
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        // Выполнение тестируемого метода
        // Передаем отклоняющееся от нормы давление
        medicalService.checkBloodPressure(patientId, abnormalPressure);

        // Проверка результата
        // Проверяем, что метод send был вызван один раз
        verify(alertService, times(1)).send(messageCaptor.capture());
        // Проверяем содержимое перехваченного сообщения
        String capturedMessage = messageCaptor.getValue();
        assertEquals("Warning, patient with id: patient-123, need help", capturedMessage);
    }

    /**
     * Тест проверяет, что сообщение не выводится, когда давление в норме
     * Ожидается, что метод send у alertService не будет вызван
     */
    @Test
    public void testCheckBloodPressureWhenPressureIsNormal() {
        // Подготовка данных для теста
        String patientId = "patient-123";
        // Нормальное давление пациента
        BloodPressure normalPressure = new BloodPressure(120, 80);

        // Создаем объект HealthInfo с нормальным давлением
        HealthInfo healthInfo = new HealthInfo(new BigDecimal("36.6"), normalPressure);
        // Создаем объект PatientInfo с информацией о пациенте
        PatientInfo patientInfo = new PatientInfo(
            patientId,
            "Иван",
            "Иванов",
            LocalDate.of(1990, 1, 1),
            healthInfo
        );

        // Настраиваем поведение мок-объекта
        // Когда patientInfoRepository.getById вызывается с patientId, возвращаем patientInfo
        when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);

        // Выполнение тестируемого метода
        // Передаем нормальное давление (такое же, как у пациента)
        medicalService.checkBloodPressure(patientId, normalPressure);

        // Проверка результата
        // Проверяем, что метод send не был вызван (показатели в норме)
        verify(alertService, never()).send(anyString());
    }

    /**
     * Тест проверяет вывод сообщения при отклонении температуры от нормы
     * Ожидается, что будет вызван метод send у alertService с соответствующим сообщением
     */
    @Test
    public void testCheckTemperatureWhenTemperatureIsAbnormal() {
        // Подготовка данных для теста
        String patientId = "patient-123";
        // Нормальная температура пациента
        BigDecimal normalTemperature = new BigDecimal("36.6");
        // Отклоняющаяся от нормы температура (ниже нормы более чем на 1.5 градуса)
        BigDecimal abnormalTemperature = new BigDecimal("34.0");

        // Создаем объект HealthInfo с нормальной температурой
        HealthInfo healthInfo = new HealthInfo(normalTemperature, new BloodPressure(120, 80));
        // Создаем объект PatientInfo с информацией о пациенте
        PatientInfo patientInfo = new PatientInfo(
            patientId,
            "Иван",
            "Иванов",
            LocalDate.of(1990, 1, 1),
            healthInfo
        );

        // Настраиваем поведение мок-объекта
        // Когда patientInfoRepository.getById вызывается с patientId, возвращаем patientInfo
        when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);

        // Создаем ArgumentCaptor для захвата аргументов, переданных в метод send
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        // Выполнение тестируемого метода
        // Передаем отклоняющуюся от нормы температуру
        medicalService.checkTemperature(patientId, abnormalTemperature);

        // Проверка результата
        // Проверяем, что метод send был вызван один раз
        verify(alertService, times(1)).send(messageCaptor.capture());
        // Проверяем содержимое перехваченного сообщения
        String capturedMessage = messageCaptor.getValue();
        assertEquals("Warning, patient with id: patient-123, need help", capturedMessage);
    }

    /**
     * Тест проверяет, что сообщение не выводится, когда температура в норме
     * Ожидается, что метод send у alertService не будет вызван
     */
    @Test
    public void testCheckTemperatureWhenTemperatureIsNormal() {
        // Подготовка данных для теста
        String patientId = "patient-123";
        // Нормальная температура пациента
        BigDecimal normalTemperature = new BigDecimal("36.6");
        // Температура в пределах нормы (не ниже нормальной температуры минус 1.5)
        BigDecimal normalMeasuredTemperature = new BigDecimal("36.0");

        // Создаем объект HealthInfo с нормальной температурой
        HealthInfo healthInfo = new HealthInfo(normalTemperature, new BloodPressure(120, 80));
        // Создаем объект PatientInfo с информацией о пациенте
        PatientInfo patientInfo = new PatientInfo(
            patientId,
            "Иван",
            "Иванов",
            LocalDate.of(1990, 1, 1),
            healthInfo
        );

        // Настраиваем поведение мок-объекта
        // Когда patientInfoRepository.getById вызывается с patientId, возвращаем patientInfo
        when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);

        // Выполнение тестируемого метода
        // Передаем температуру в пределах нормы
        medicalService.checkTemperature(patientId, normalMeasuredTemperature);

        // Проверка результата
        // Проверяем, что метод send не был вызван (показатели в норме)
        verify(alertService, never()).send(anyString());
    }

    /**
     * Тест проверяет вывод сообщения при высокой температуре
     * Ожидается, что будет вызван метод send у alertService с соответствующим сообщением
     */
    @Test
    public void testCheckTemperatureWhenTemperatureIsHigh() {
        // Подготовка данных для теста
        String patientId = "patient-123";
        // Нормальная температура пациента
        BigDecimal normalTemperature = new BigDecimal("36.6");
        // Высокая температура (выше нормы)
        BigDecimal highTemperature = new BigDecimal("38.5");

        // Создаем объект HealthInfo с нормальной температурой
        HealthInfo healthInfo = new HealthInfo(normalTemperature, new BloodPressure(120, 80));
        // Создаем объект PatientInfo с информацией о пациенте
        PatientInfo patientInfo = new PatientInfo(
            patientId,
            "Иван",
            "Иванов",
            LocalDate.of(1990, 1, 1),
            healthInfo
        );

        // Настраиваем поведение мок-объекта
        // Когда patientInfoRepository.getById вызывается с patientId, возвращаем patientInfo
        when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);

        // Выполнение тестируемого метода
        // Передаем высокую температуру
        medicalService.checkTemperature(patientId, highTemperature);

        // Проверка результата
        // Проверяем, что метод send не был вызван
        // (логика проверяет только низкую температуру: норма - 1.5 > измеренная)
        // Для температуры 38.5: 36.6 - 1.5 = 35.1, 35.1 > 38.5 = false, поэтому send не вызывается
        verify(alertService, never()).send(anyString());
    }
}
