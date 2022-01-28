package engine.collection;

public interface Collection<T> extends Iterable<T> {
    int size();

    default boolean isEmpty() {
        return size() <= 0;
    }

    default boolean isNotEmpty() {
        return size() > 0;
    }
}
