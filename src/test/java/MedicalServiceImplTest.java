import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


public class MedicalServiceImplTest {
    @Test
    public void checkBloodPressureTest () {
        String expectedMessage = "Warning, patient with id: null, need help";
        BloodPressure bloodPressure = new BloodPressure(60, 120);

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.any())).thenReturn(new PatientInfo("Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78))));

        SendAlertService sendAlertService = Mockito.spy(SendAlertServiceImpl.class);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure(null, bloodPressure);

        Mockito.verify(sendAlertService).send(argumentCaptor.capture());
        assertThat(expectedMessage, comparesEqualTo(argumentCaptor.getValue()));
    }

    @Test
    public void checkTemperatureTest () {
        String expectedMessage = "Warning, patient with id: null, need help";
        BigDecimal temperature = new BigDecimal("35");

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.any())).thenReturn(new PatientInfo("Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78))));

        SendAlertService sendAlertService = Mockito.spy(SendAlertServiceImpl.class);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkTemperature(null, temperature);

        Mockito.verify(sendAlertService).send(argumentCaptor.capture());
        assertThat(expectedMessage, comparesEqualTo(argumentCaptor.getValue()));
    }
    @Test
    public void getPatientInfoIfIdNullTest () {
        String patientId = "";
        PatientInfoRepository  patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(patientId)).thenThrow(new RuntimeException("Patient not found"));
    }
    @Test
    public void checkTemperatureAndBloodPressureTest () {
        String expectedMessage = "Warning, patient with id: null, need help";
        BigDecimal temperature = new BigDecimal("36.6");
        BloodPressure bloodPressure = new BloodPressure(125, 78);

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.any())).thenReturn(new PatientInfo("Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78))));

        SendAlertService sendAlertService = Mockito.spy(SendAlertServiceImpl.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkTemperature(null, temperature);
        medicalService.checkBloodPressure(null, bloodPressure);

        Mockito.verifyZeroInteractions(sendAlertService);
    }
}
