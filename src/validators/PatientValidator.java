package validators;

import domain.Patient;

public class PatientValidator implements Validator<Patient> {
    @Override
    public void validate(Patient patient) throws ValidationException {
        if (patient.getID() == null || patient.getID() <= 0) {
            throw new ValidationException("Invalid Patient ID: Must be a positive integer.");
        }
        if (patient.getName() == null || patient.getName().isEmpty()) {
            throw new ValidationException("Invalid Patient Name: Cannot be null or empty.");
        }
        if (patient.getAddress() == null || patient.getAddress().isEmpty()) {
            throw new ValidationException("Invalid Patient Address: Cannot be null or empty.");
        }
        if (patient.getPhone() == null || !patient.getPhone().matches("\\d{10}")) {
            throw new ValidationException("Invalid Patient Phone: Must be a 10-digit number.");
        }
    }
}
