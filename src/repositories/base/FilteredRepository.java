package repositories.base;

import filters.Filter;
import java.util.HashMap;
import java.util.Map;
import domain.Identifiable;

public class FilteredRepository<ID, T extends Identifiable<ID>> {
    private final IRepository<ID, T> repository;

    public FilteredRepository(IRepository<ID, T> repository) {
        this.repository = repository;
    }

    public Map<ID, T> filterEntities(Filter<T> filter) {
        Map<ID, T> result = new HashMap<>();

        repository.getAll().forEach((id, entity) -> {
            if (filter.accept(entity)) {
                result.put(id, entity);
            }
        });

        return result;
    }
}
