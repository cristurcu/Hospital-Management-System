package services;

import repositories.base.IRepository;
import validators.ValidationException;
import validators.Validator;

import java.util.Map;
import java.util.Optional;

public class Service<ID, T> {
    private final IRepository<ID, T> repo;
    private final Validator<T> validator;

    public Service(IRepository<ID, T> repo, Validator<T> validator) {
        this.repo = repo;
        this.validator = validator;
    }

    public boolean addEntity(ID id, T entity) {
        try {
            validator.validate(entity);
            repo.add(id, entity);
            return true;
        } catch (ValidationException e) {
            System.err.println("Validation failed: " + e.getMessage());
            return false;
        }
    }

    public Optional<T> deleteEntity(ID id) {
        return repo.delete(id);
    }

    public boolean modifyEntity(ID id, T entity) {
        try {
            validator.validate(entity);
            repo.modify(id, entity);
            return true;
        } catch (ValidationException e) {
            System.err.println("Validation failed: " + e.getMessage());
            return false;
        }
    }

    public Optional<T> findEntityById(ID id) {
        return repo.findById(id);
    }

    public Map<ID, T> getAllEntities() {
        return Map.copyOf(repo.getAll());
    }
}
