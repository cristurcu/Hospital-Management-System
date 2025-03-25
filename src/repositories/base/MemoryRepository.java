package repositories.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import domain.Identifiable;

public class MemoryRepository<ID, T extends Identifiable<ID>> implements IRepository<ID, T> {
    protected HashMap<ID, T> elements = new HashMap<>();

    @Override
    public void add(ID id, T entity) {
        elements.put(id, entity);
    }

    @Override
    public Optional<T> delete(ID id) {
        T removedEntity = elements.remove(id);
        return Optional.ofNullable(removedEntity);
    }

    @Override
    public void modify(ID id, T entity) {
        elements.put(id, entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(elements.get(id));
    }

    @Override
    public Map<ID, T> getAll() {
        return elements;
    }
}
