import domain.Patient;
import org.junit.jupiter.api.Test;
import repositories.base.IRepository;
import repositories.base.RepositoryFactory;
import services.Service;
import validators.PatientValidator;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {
    IRepository<Integer, Patient> patientRepo = RepositoryFactory.createPatientRepository();
    private final Service<Integer, Patient> patientService = new Service<>(patientRepo, new PatientValidator());

    @Test
    void testAddEntity() {
        Patient patient = new Patient(1, "Maria Popescu", "Libertății 15", "0712345678");
        patientService.addEntity(1, patient);

        assertEquals(1, patientService.getAllEntities().size());
        assertEquals(patient, patientService.findEntityById(1));
    }

    @Test
    void testDeleteEntity() {
        Patient patient = new Patient(1, "Ion Ionescu", "Victoriei 10", "0711122233");
        patientService.addEntity(1, patient);

        patientService.deleteEntity(1);

        assertTrue(patientService.getAllEntities().isEmpty());
        assertNull(patientService.findEntityById(1));
    }

    @Test
    void testModifyEntity() {
        Patient patient = new Patient(1, "Ana Vasile", "Florilor 20", "0722334455");
        patientService.addEntity(1, patient);

        Patient updatedPatient = new Patient(1, "Ana Georgescu", "Trandafirilor 25", "0722667788");
        patientService.modifyEntity(1, updatedPatient);

        Optional<Patient> retrievedPatient = patientService.findEntityById(1);
        assertNotNull(retrievedPatient);
        assertEquals("Ana Georgescu", retrievedPatient.get().getName());
        assertEquals("Trandafirilor 25", retrievedPatient.get().getAddress());
        assertEquals("0722667788", retrievedPatient.get().getPhone());
    }

    @Test
    void testFindEntityById() {
        Patient patient1 = new Patient(1, "Radu Mihai", "Principală 5", "0744556677");
        Patient patient2 = new Patient(2, "Elena Dumitru", "Secundară 8", "0755667788");
        patientService.addEntity(1, patient1);
        patientService.addEntity(2, patient2);

        assertEquals(patient1, patientService.findEntityById(1));
        assertEquals(patient2, patientService.findEntityById(2));
        assertNull(patientService.findEntityById(3)); // Non-existent ID
    }

    @Test
    void testGetAllEntities() {
        Patient patient1 = new Patient(1, "Gheorghe Andrei", "Câmpului 9", "0766778899");
        Patient patient2 = new Patient(2, "Ioana Marin", "Griviței 3", "0777889900");
        patientService.addEntity(1, patient1);
        patientService.addEntity(2, patient2);

        Map<Integer, Patient> allPatients = patientService.getAllEntities();
        assertEquals(2, allPatients.size());
        assertTrue(allPatients.containsKey(1));
        assertTrue(allPatients.containsKey(2));
        assertEquals(patient1, allPatients.get(1));
        assertEquals(patient2, allPatients.get(2));
    }

}
