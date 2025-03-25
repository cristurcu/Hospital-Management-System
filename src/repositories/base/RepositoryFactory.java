package repositories.base;

import domain.Appointment;
import domain.Patient;
import repositories.database.AppointmentDatabaseRepository;
import repositories.database.PatientDatabaseRepository;
import repositories.file.BinaryFileRepository;
import repositories.file.TextFileRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class RepositoryFactory {

    public static IRepository<Integer, Patient> createPatientRepository() {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("src/config/settings.properties"));
            String repoType = properties.getProperty("repo_type");
            String repoPath = properties.getProperty("patient_repo_path");

            return switch (repoType) {
                case "memory" -> new MemoryRepository<>();
                case "text" -> new TextFileRepository<>(repoPath);
                case "binary" -> new BinaryFileRepository<>(repoPath);
                case "database" -> new PatientDatabaseRepository();
                default -> throw new RuntimeException("Invalid repository type in settings.properties.");
            };

        } catch (IOException e) {
            throw new RuntimeException("Error reading settings.properties: " + e.getMessage(), e);
        }
    }

    public static IRepository<Integer, Appointment> createAppointmentRepository() {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("src/config/settings.properties"));
            String repoType = properties.getProperty("repo_type");
            String repoPath = properties.getProperty("appointment_repo_path");

            return switch (repoType) {
                case "memory" -> new MemoryRepository<>();
                case "text" -> new TextFileRepository<>(repoPath);
                case "binary" -> new BinaryFileRepository<>(repoPath);
                case "database" -> new AppointmentDatabaseRepository();
                default -> throw new RuntimeException("Invalid repository type in settings.properties.");
            };

        } catch (IOException e) {
            throw new RuntimeException("Error reading settings.properties: " + e.getMessage(), e);
        }
    }
}
