package filters;

import domain.Doctor;

public class FilterBySpecialty implements Filter<Doctor> {
    private final String specialty;
    public FilterBySpecialty(String specialty) {
        this.specialty = specialty;
    }

    @Override
    public boolean accept(Doctor doctor) {
        return doctor.getSpeciality().equals(specialty);
    }

}
