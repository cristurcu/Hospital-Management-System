package repositories.base;

import java.util.Map;
import java.util.Optional;

public interface IRepository<ID, T> {
    void add(ID id, T entity);
    Optional<T> delete(ID id);
    void modify(ID id, T entity);
    Optional<T> findById(ID id);
    Map<ID, T> getAll();
}
