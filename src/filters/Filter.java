package filters;

public interface Filter<T>{
    boolean accept(T item);
}
