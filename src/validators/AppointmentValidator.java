package validators;

import domain.Appointment;

public class AppointmentValidator implements Validator<Appointment> {
    @Override
    public void validate(Appointment appointment) throws ValidationException {
        if (appointment.getID() == null || appointment.getID() <= 0) {
            throw new ValidationException("Invalid Appointment ID: Must be a positive integer.");
        }
        if (appointment.getDate() == null) {
            throw new ValidationException("Invalid Appointment Date: Cannot be null.");
        }
        if (appointment.getDate().isBefore(java.time.LocalDate.now())) {
            throw new ValidationException("Invalid Appointment Date: Cannot be in the past.");
        }
    }
}
