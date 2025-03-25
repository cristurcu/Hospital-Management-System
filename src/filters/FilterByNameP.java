package filters;

import domain.Patient;

public class FilterByNameP implements Filter<Patient> {
    private final String name;
    public FilterByNameP(String name) {
        this.name = name;
    }

    @Override
    public boolean accept(Patient patient) {
        return patient.getName().equalsIgnoreCase(name);
    }
}
