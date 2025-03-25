package filters;

import domain.Appointment;

import java.time.LocalDate;

public class FilterByDate implements Filter<Appointment> {
    private final LocalDate date;
    public FilterByDate(LocalDate date) {
        this.date = date;
    }
    @Override
    public boolean accept(Appointment appointment) {
        return appointment.getDate().equals(date);
    }

}
