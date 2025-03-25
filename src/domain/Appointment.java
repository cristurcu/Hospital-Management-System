package domain;
import java.time.LocalDate;
import java.io.Serializable;

public class Appointment implements Identifiable<Integer>, Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id = -1;
    private LocalDate date = LocalDate.now();
    private Integer patientId;

    public Appointment(Integer id, Integer patientId, LocalDate date) {
        this.id = id;
        this.patientId = patientId;
        this.date = date;
    }

    public Appointment() {}

    public LocalDate getDate() {
        return date;
    }

    public String toString() {
        return "Appointment id = " + id + ", date = " + date;
    }

    @Override
    public Integer getID() {
        return id;
    }

    public Integer getPatientId() {
        return patientId;
    }
}
