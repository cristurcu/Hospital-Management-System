package ui;

import domain.Appointment;
import domain.Patient;
import filters.Filter;
import filters.FilterByDate;
import filters.FilterByNameP;
import repositories.base.FilteredRepository;
import repositories.base.IRepository;
import repositories.base.RepositoryFactory;
import services.Service;
import validators.AppointmentValidator;
import validators.PatientValidator;
import reports.Reports;
import java.time.LocalDate;
import java.util.Map;
import java.util.Scanner;

public class UI {
    private static final Scanner scanner = new Scanner(System.in);
    IRepository<Integer, Patient> patientRepo = RepositoryFactory.createPatientRepository();
    IRepository<Integer, Appointment> appointmentRepo = RepositoryFactory.createAppointmentRepository();
    private final Service<Integer, Patient> patientService = new Service<>(patientRepo, new PatientValidator());
    private final Service<Integer, Appointment> appointmentService = new Service<>(appointmentRepo, new AppointmentValidator());
    private final Reports reports = new Reports(patientService, appointmentService);

    public void display() {
            try {
                while (true) {
                    System.out.println("\nMenu:");
                    System.out.println("1. Manage Patients");
                    System.out.println("2. Manage Appointments");
                    System.out.println("3. View Reports");
                    System.out.println("4. Exit");
                    System.out.print("Choose an option: ");
                    int option = scanner.nextInt();
                    scanner.nextLine();

                    switch (option) {
                        case 1 -> managePatients();
                        case 2 -> manageAppointments();
                        case 3 -> manageReports();
                        case 4 -> {
                            System.out.println("Exiting...");
                            return;
                        }
                        default -> System.out.println("Invalid option. Please try again.");
                    }
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
    }


    private void manageReports() {
        System.out.println("\nReports Menu:");
        System.out.println("1. Get Patient Address by ID");
        System.out.println("2. Get Patient Phone by ID");
        System.out.println("3. Get Patient Name by ID");
        System.out.println("4. Get Appointment Date by ID");
        System.out.println("5. Get Patient Name by Appointment ID");
        System.out.print("Choose a report: ");
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1 -> getPatientAddressReport();
            case 2 -> getPatientPhoneReport();
            case 3 -> getPatientNameByIdReport();
            case 4 -> getAppointmentDateReport();
            case 5 -> getPatientNameByAppointmentIdReport();
            default -> System.out.println("Invalid option. Please try again.");
        }
    }

    private void getPatientAddressReport() {
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();

        reports.getPatientAddress(patientId)
                .ifPresentOrElse(
                        address -> System.out.println("Patient Address: " + address),
                        () -> System.out.println("Patient not found.")
                );
    }

    private void getPatientPhoneReport() {
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();

        reports.getPatientPhone(patientId)
                .ifPresentOrElse(
                        phone -> System.out.println("Patient Phone: " + phone),
                        () -> System.out.println("Patient not found.")
                );
    }

    private void getPatientNameByIdReport() {
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();

        reports.getPatientNameById(patientId)
                .ifPresentOrElse(
                        name -> System.out.println("Patient Name: " + name),
                        () -> System.out.println("Patient not found.")
                );
    }

    private void getAppointmentDateReport() {
        System.out.print("Enter Appointment ID: ");
        int appointmentId = scanner.nextInt();
        scanner.nextLine();

        reports.getAppointmentDate(appointmentId)
                .ifPresentOrElse(
                        date -> System.out.println("Appointment Date: " + date),
                        () -> System.out.println("Appointment not found.")
                );
    }

    private void getPatientNameByAppointmentIdReport() {
        System.out.print("Enter Appointment ID: ");
        int appointmentId = scanner.nextInt();
        scanner.nextLine();

        reports.getPatientNameByAppointmentId(appointmentId)
                .ifPresentOrElse(
                        name -> System.out.println("Patient Name: " + name),
                        () -> System.out.println("Appointment or Patient not found.")
                );
    }

    private void managePatients() {
        System.out.println("\nPatient Management:");
        System.out.println("1. Add Patient");
        System.out.println("2. View All Patients");
        System.out.println("3. Find Patient by ID");
        System.out.println("4. Delete Patient");
        System.out.println("5. Modify Patient");
        System.out.println("6. Filter Patients by Name");
        System.out.print("Choose an option: ");
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1 -> addPatient();
            case 2 -> viewAllPatients();
            case 3 -> findPatientById();
            case 4 -> deletePatient();
            case 5 -> modifyPatient();
            case 6 -> filterPatientsByName();
            default -> System.out.println("Invalid option.");
        }
    }

    private void addPatient() {
        System.out.print("Enter patient ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter patient name: ");
        String name = scanner.nextLine();
        System.out.print("Enter patient address: ");
        String address = scanner.nextLine();
        System.out.print("Enter patient phone: ");
        String phone = scanner.nextLine();

        Patient patient = new Patient(id, name, address, phone);
        if (patientService.addEntity(id, patient)) {
            System.out.println("Patient added successfully.");
        } else {
            System.out.println("Failed to add patient. Please check your input.");
        }
    }

    private void viewAllPatients() {
        System.out.println("All Patients:");
        Map<Integer, Patient> patients = patientService.getAllEntities();
        if (patients.isEmpty()) {
            System.out.println("No patients available.");
        } else {
            patients.values().forEach(System.out::println);
        }
    }

    private void findPatientById() {
        System.out.print("Enter the patient ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        patientService.findEntityById(id)
                .ifPresentOrElse(
                        patient -> System.out.println("Patient found: " + patient),
                        () -> System.out.println("Patient with ID " + id + " not found.")
                );
    }

    private void deletePatient() {
        System.out.print("Enter the patient ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        if (patientService.deleteEntity(id).isPresent()) {
            System.out.println("Patient with ID " + id + " has been deleted.");
        } else {
            System.out.println("Patient with ID " + id + " not found.");
        }
    }

    private void modifyPatient() {
        System.out.print("Enter the patient ID to modify: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        patientService.findEntityById(id).ifPresentOrElse(existingPatient -> {
            System.out.println("Existing patient: " + existingPatient);
            System.out.print("Enter new patient name: ");
            String name = scanner.nextLine();
            System.out.print("Enter new patient address: ");
            String address = scanner.nextLine();
            System.out.print("Enter new patient phone: ");
            String phone = scanner.nextLine();

            Patient modifiedPatient = new Patient(id, name, address, phone);
            if (patientService.modifyEntity(id, modifiedPatient)) {
                System.out.println("Patient with ID " + id + " has been updated.");
            } else {
                System.out.println("Failed to update patient. Please check your input.");
            }
        }, () -> System.out.println("Patient with ID " + id + " not found."));
    }

    private void filterPatientsByName() {
        System.out.print("Enter the name to filter by: ");
        String name = scanner.nextLine();

        FilteredRepository<Integer, Patient> filteredPatientRepo = new FilteredRepository<>(patientRepo);
        Filter<Patient> nameFilter = new FilterByNameP(name);
        Map<Integer, Patient> filteredPatients = filteredPatientRepo.filterEntities(nameFilter);

        if (filteredPatients.isEmpty()) {
            System.out.println("No patients found with the name: " + name);
        } else {
            System.out.println("Filtered Patients:");
            filteredPatients.values().forEach(System.out::println);
        }
    }

    private void manageAppointments() {
        System.out.println("\nAppointment Management:");
        System.out.println("1. Add Appointment");
        System.out.println("2. View All Appointments");
        System.out.println("3. Find Appointment by ID");
        System.out.println("4. Delete Appointment");
        System.out.println("5. Modify Appointment");
        System.out.println("6. Filter Appointments by Date");
        System.out.print("Choose an option: ");
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1 -> addAppointment();
            case 2 -> viewAllAppointments();
            case 3 -> findAppointmentById();
            case 4 -> deleteAppointment();
            case 5 -> modifyAppointment();
            case 6 -> filterAppointmentsByDate();
            default -> System.out.println("Invalid option.");
        }
    }

    private void addAppointment() {
        System.out.print("Enter appointment ID: ");
        int id = scanner.nextInt();
        System.out.print("Enter patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter appointment date (yyyy-mm-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        Appointment appointment = new Appointment(id, patientId, date);
        if (appointmentService.addEntity(id, appointment)) {
            System.out.println("Appointment added successfully.");
        } else {
            System.out.println("Failed to add appointment. Please check your input.");
        }
    }

    private void viewAllAppointments() {
        System.out.println("All Appointments:");
        Map<Integer, Appointment> appointments = appointmentService.getAllEntities();
        if (appointments.isEmpty()) {
            System.out.println("No appointments available.");
        } else {
            appointments.values().forEach(System.out::println);
        }
    }

    private void findAppointmentById() {
        System.out.print("Enter appointment ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        appointmentService.findEntityById(id)
                .ifPresentOrElse(
                        appointment -> System.out.println("Found Appointment: " + appointment),
                        () -> System.out.println("Appointment not found.")
                );
    }

    private void deleteAppointment() {
        System.out.print("Enter appointment ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        if (appointmentService.deleteEntity(id).isPresent()) {
            System.out.println("Appointment with ID " + id + " has been deleted.");
        } else {
            System.out.println("Appointment with ID " + id + " not found.");
        }
    }

    private void modifyAppointment() {
        System.out.print("Enter appointment ID to modify: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        appointmentService.findEntityById(id).ifPresentOrElse(existingAppointment -> {
            System.out.print("Enter new patient ID: ");
            int newPatientId = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter new appointment date (yyyy-mm-dd): ");
            LocalDate newDate = LocalDate.parse(scanner.nextLine());

            Appointment modifiedAppointment = new Appointment(id, newPatientId, newDate);
            if (appointmentService.modifyEntity(id, modifiedAppointment)) {
                System.out.println("Appointment with ID " + id + " has been updated.");
            } else {
                System.out.println("Failed to modify appointment.");
            }
        }, () -> System.out.println("Appointment with ID " + id + " not found."));
    }

    private void filterAppointmentsByDate() {
        System.out.print("Enter the date to filter by (yyyy-mm-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        FilteredRepository<Integer, Appointment> filteredAppointmentRepo = new FilteredRepository<>(appointmentRepo);
        Filter<Appointment> dateFilter = new FilterByDate(date);
        Map<Integer, Appointment> filteredAppointments = filteredAppointmentRepo.filterEntities(dateFilter);

        if (filteredAppointments.isEmpty()) {
            System.out.println("No appointments found for the date: " + date);
        } else {
            filteredAppointments.values().forEach(System.out::println);
        }
    }
}
