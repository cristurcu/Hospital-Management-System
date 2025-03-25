package repositories.file;

import domain.Appointment;
import domain.Identifiable;
import domain.Patient;
import repositories.base.IRepository;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@SuppressWarnings("ALL")
public class TextFileRepository<ID, T extends Identifiable<ID>> implements IRepository<ID, T> {
    private final String filename;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TextFileRepository(String filename){
        this.filename = filename;
    }

    @Override
    public void add(ID id, T entity) {
        Map<ID, T> elements = getAll();
        if (elements.containsKey(id)) {
            throw new IllegalArgumentException("Entity with ID already exists.");
        }
        elements.put(id, entity);
        writeToFile(elements);
    }

    @Override
    public Optional<T> delete(ID id) {
        Map<ID, T> elements = getAll();
        return Optional.ofNullable(elements.remove(id))
                .map(deletedEntity -> {
                    writeToFile(elements);
                    return deletedEntity;
                });
    }

    @Override
    public void modify(ID id, T entity) {
        Map<ID, T> elements = getAll();
        if (!elements.containsKey(id)) {
            throw new IllegalArgumentException("Entity with ID does not exist.");
        }
        elements.put(id, entity);
        writeToFile(elements);
    }

    @Override
    public Optional<T> findById(ID id) {
        Map<ID, T> elements = getAll();
        return Optional.ofNullable(elements.get(id));
    }

    @Override
    public Map<ID, T> getAll() {
        Map<ID, T> elements = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                T entity = parseEntity(line);
                elements.put(entity.getID(), entity);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading text file", e);
        }
        return elements;
    }


    protected T parseEntity(String line) {
        String[] parts = line.split(",");
        if (parts.length == 3) {
            try {
                int id = Integer.parseInt(parts[0]);
                int patientId = Integer.parseInt(parts[1]);
                LocalDate date = LocalDate.parse(parts[2], formatter);
                return (T) new Appointment(id, patientId, date);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid Appointment format: " + line, e);
            }
        } else if (parts.length == 4) {
            try {
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String address = parts[2];
                String phoneNumber = parts[3];
                return (T) new Patient(id, name, address, phoneNumber);
            } catch (RuntimeException e) {
                throw new RuntimeException("Invalid Patient format: " + line, e);
            }
        } else {
            throw new IllegalArgumentException("Invalid line format: " + line);
        }
    }

    protected String serializeEntity(T entity) {
        if (entity instanceof Appointment appointment) {
            return appointment.getID() + "," + appointment.getPatientId() + "," + appointment.getDate();
        } else if (entity instanceof Patient patient) {
            return patient.getID() + "," + patient.getName() + "," + patient.getAddress() + "," + patient.getPhone();
        }
        throw new IllegalArgumentException("Unsupported entity type");
    }

    private void writeToFile(Map<ID, T> elements) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (T entity : elements.values()) {
                bw.write(serializeEntity(entity));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing to text file: " + e.getMessage(), e);
        }
    }
}
