package reports;

import domain.Appointment;
import domain.Patient;
import services.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Reports {
    private final Service<Integer, Patient> patientService;
    private final Service<Integer, Appointment> appointmentService;

    public Reports(Service<Integer, Patient> patientService, Service<Integer, Appointment> appointmentService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }

    public Optional<LocalDate> getAppointmentDate(int appointmentId) {
        return appointmentService.getAllEntities().values()
                .stream()
                .filter(appointment -> appointment.getID() == appointmentId)
                .map(Appointment::getDate)
                .findFirst();
    }

    public Optional<String> getPatientAddress(int patientId) {
        return patientService.getAllEntities().entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .filter(patient -> patient.getID() == patientId)
                .map(Patient::getAddress)
                .findFirst();
    }

    public Optional<String> getPatientPhone(int patientId) {
        return patientService.getAllEntities().entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .filter(patient -> patient.getID() == patientId)
                .map(Patient::getPhone)
                .findFirst();
    }


    public Optional<String> getPatientNameById(int patientId) {
        return patientService.getAllEntities().entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .filter(patient -> patient.getID() == patientId)
                .map(Patient::getName)
                .findFirst();
    }

    public Optional<String> getPatientNameByAppointmentId(int appointmentId) {
        return appointmentService.getAllEntities().entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .filter(app -> app.getID() == appointmentId)
                .flatMap(app -> patientService.getAllEntities().entrySet()
                        .stream()
                        .map(Map.Entry::getValue)
                        .filter(patient -> Objects.equals(patient.getID(), app.getPatientId()))
                        .map(Patient::getName)
                )
                .findFirst();
    }


}
