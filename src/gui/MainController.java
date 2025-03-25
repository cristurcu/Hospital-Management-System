package gui;

import domain.Appointment;
import domain.Patient;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import reports.Reports;
import services.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class MainController {
    private  Service<Integer, Patient> patientService;
    private  Service<Integer, Appointment> appointmentService;
    private Reports reports;

    @FXML
    private TextField idField, nameField, addressField, phoneField;
    @FXML
    private TextField appointmentIdField, patientIdField, appointmentDateField;
    @FXML
    private TextField reportInputField;
    @FXML
    private TextArea outputArea;


    public void initServices(Service<Integer, Patient> patientService, Service<Integer, Appointment> appointmentService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.reports = new Reports(patientService, appointmentService);
    }

    @FXML
    private void handleAddPatient() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String address = addressField.getText();
            String phone = phoneField.getText();
            Patient patient = new Patient(id, name, address, phone);
            patientService.addEntity(id, patient);
            outputArea.setText("Patient added: " + patient);
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewPatients() {
        Map<Integer, Patient> patients = patientService.getAllEntities();
        if (patients.isEmpty()) {
            outputArea.setText("No patients found.");
        } else {
            StringBuilder sb = new StringBuilder("Patients:\n");
            patients.values().forEach(patient -> sb.append(patient).append("\n"));
            outputArea.setText(sb.toString());
        }
    }

    @FXML
    private void handleFindPatient() {
        try {
            int id = Integer.parseInt(idField.getText());
            patientService.findEntityById(id).ifPresentOrElse(
                    patient -> outputArea.setText("Found Patient: " + patient),
                    () -> outputArea.setText("Patient not found!")
            );
        } catch (NumberFormatException e) {
            outputArea.setText("Invalid ID format.");
        }
    }

    @FXML
    private void handleDeletePatient() {
        try {
            int id = Integer.parseInt(idField.getText());
            patientService.deleteEntity(id).ifPresentOrElse(
                    patient -> outputArea.setText("Deleted Patient: " + patient),
                    () -> outputArea.setText("Patient not found!")
            );
        } catch (NumberFormatException e) {
            outputArea.setText("Invalid ID format.");
        }
    }

    @FXML
    private void handleModifyPatient() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String address = addressField.getText();
            String phone = phoneField.getText();
            Patient updatedPatient = new Patient(id, name, address, phone);
            patientService.modifyEntity(id, updatedPatient);
            outputArea.setText("Updated Patient: " + updatedPatient);
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleFilterPatients() {
        String name = nameField.getText();
        Map<Integer, Patient> filteredPatients = patientService.getAllEntities().entrySet().stream()
                .filter(entry -> entry.getValue().getName().contains(name))
                .collect(java.util.stream.Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (filteredPatients.isEmpty()) {
            outputArea.setText("No patients found with the name: " + name);
        } else {
            StringBuilder sb = new StringBuilder("Filtered Patients:\n");
            filteredPatients.values().forEach(patient -> sb.append(patient).append("\n"));
            outputArea.setText(sb.toString());
        }
    }

    @FXML
    private void handleAddAppointment() {
        try {
            int id = Integer.parseInt(appointmentIdField.getText());
            int patientId = Integer.parseInt(patientIdField.getText());
            LocalDate date = LocalDate.parse(appointmentDateField.getText());
            Appointment appointment = new Appointment(id, patientId, date);
            appointmentService.addEntity(id, appointment);
            outputArea.setText("Appointment added: " + appointment);
        } catch (NumberFormatException | DateTimeParseException e) {
            outputArea.setText("Invalid input: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewAppointments() {
        Map<Integer, Appointment> appointments = appointmentService.getAllEntities();
        if (appointments.isEmpty()) {
            outputArea.setText("No appointments found.");
        } else {
            StringBuilder sb = new StringBuilder("Appointments:\n");
            appointments.values().forEach(appointment -> sb.append(appointment).append("\n"));
            outputArea.setText(sb.toString());
        }
    }

    @FXML
    private void handleFindAppointment() {
        try {
            int id = Integer.parseInt(appointmentIdField.getText());
            appointmentService.findEntityById(id).ifPresentOrElse(
                    appointment -> outputArea.setText("Found Appointment: " + appointment),
                    () -> outputArea.setText("Appointment not found!")
            );
        } catch (NumberFormatException e) {
            outputArea.setText("Invalid ID format.");
        }
    }

    @FXML
    private void handleDeleteAppointment() {
        try {
            int id = Integer.parseInt(appointmentIdField.getText());
            appointmentService.deleteEntity(id).ifPresentOrElse(
                    appointment -> outputArea.setText("Deleted Appointment: " + appointment),
                    () -> outputArea.setText("Appointment not found!")
            );
        } catch (NumberFormatException e) {
            outputArea.setText("Invalid ID format.");
        }
    }

    @FXML
    private void handleModifyAppointment() {
        try {
            int id = Integer.parseInt(appointmentIdField.getText());
            int patientId = Integer.parseInt(patientIdField.getText());
            LocalDate date = LocalDate.parse(appointmentDateField.getText());
            Appointment updatedAppointment = new Appointment(id, patientId, date);
            appointmentService.modifyEntity(id, updatedAppointment);
            outputArea.setText("Updated Appointment: " + updatedAppointment);
        } catch (NumberFormatException | DateTimeParseException e) {
            outputArea.setText("Invalid input: " + e.getMessage());
        }
    }

    @FXML
    private void handleFilterAppointments() {
        try {
            LocalDate date = LocalDate.parse(appointmentDateField.getText());
            Map<Integer, Appointment> filteredAppointments = appointmentService.getAllEntities().entrySet().stream()
                    .filter(entry -> entry.getValue().getDate().equals(date))
                    .collect(java.util.stream.Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            if (filteredAppointments.isEmpty()) {
                outputArea.setText("No appointments found for the date: " + date);
            } else {
                StringBuilder sb = new StringBuilder("Filtered Appointments:\n");
                filteredAppointments.values().forEach(appointment -> sb.append(appointment).append("\n"));
                outputArea.setText(sb.toString());
            }
        } catch (DateTimeParseException e) {
            outputArea.setText("Invalid date format.");
        }
    }

    @FXML
    private void handlePatientAddressReport() {
        try {
            int id = Integer.parseInt(reportInputField.getText());
            reports.getPatientAddress(id).ifPresentOrElse(
                    address -> outputArea.setText("Address: " + address),
                    () -> outputArea.setText("No address found for Patient ID: " + id)
            );
        } catch (NumberFormatException e) {
            outputArea.setText("Invalid ID format.");
        }
    }

    @FXML
    private void handlePatientPhoneReport() {
        try {
            int id = Integer.parseInt(reportInputField.getText());
            reports.getPatientPhone(id).ifPresentOrElse(
                    phone -> outputArea.setText("Phone: " + phone),
                    () -> outputArea.setText("No phone found for Patient ID: " + id)
            );
        } catch (NumberFormatException e) {
            outputArea.setText("Invalid ID format.");
        }
    }

    @FXML
    private void handlePatientNameByIdReport() {
        try {
            int id = Integer.parseInt(reportInputField.getText());
            reports.getPatientNameById(id).ifPresentOrElse(
                    name -> outputArea.setText("Name: " + name),
                    () -> outputArea.setText("No name found for Patient ID: " + id)
            );
        } catch (NumberFormatException e) {
            outputArea.setText("Invalid ID format.");
        }
    }

    @FXML
    private void handleAppointmentDateReport() {
        try {
            int id = Integer.parseInt(reportInputField.getText());
            reports.getAppointmentDate(id).ifPresentOrElse(
                    date -> outputArea.setText("Appointment Date: " + date),
                    () -> outputArea.setText("No appointment found for ID: " + id)
            );
        } catch (NumberFormatException e) {
            outputArea.setText("Invalid ID format.");
        }
    }

    @FXML
    private void handlePatientNameByAppointmentIdReport() {
        try {
            int id = Integer.parseInt(reportInputField.getText());
            reports.getPatientNameByAppointmentId(id).ifPresentOrElse(
                    name -> outputArea.setText("Patient Name: " + name),
                    () -> outputArea.setText("No patient found for Appointment ID: " + id)
            );
        } catch (NumberFormatException e) {
            outputArea.setText("Invalid ID format.");
        }
    }
}
