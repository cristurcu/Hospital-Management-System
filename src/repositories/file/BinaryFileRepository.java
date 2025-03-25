package repositories.file;

import domain.Identifiable;
import repositories.base.IRepository;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BinaryFileRepository<ID, T extends Identifiable<ID>> implements IRepository<ID, T> {
    private final String filename;

    public BinaryFileRepository(String filename) {
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
                .map(entity -> {
                    writeToFile(elements);
                    return entity;
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
    @SuppressWarnings("unchecked")
    public Map<ID, T> getAll() {
        Map<ID, T> elements = new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            elements = (Map<ID, T>) ois.readObject();
        } catch (EOFException | FileNotFoundException e) {
            return elements;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error reading binary file: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return elements;
    }

    private void writeToFile(Map<ID, T> elements) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(elements);
        } catch (IOException e) {
            throw new RuntimeException("Error writing to binary file: " + e.getMessage(), e);
        }
    }


}
