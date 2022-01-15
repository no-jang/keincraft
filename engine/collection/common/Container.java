package engine.collection.common;

public interface Container<T> {
    boolean contains(T element);

    default boolean containsAll(Iterable<? extends T> elements) {
        for (T element : elements) {
            if (!contains(element)) {
                return false;
            }
        }

        return true;
    }
}
