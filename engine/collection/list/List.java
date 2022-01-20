package engine.collection.list;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.NoSuchElementException;

public interface List<T> extends Iterable<T> {
    int size();

    @Nullable
    T getOrNull(int index);

    default boolean isEmpty() {
        return size() <= 0;
    }

    default boolean isNotEmpty() {
        return size() > 0;
    }

    default T get(int index) {
        T element = getOrNull(index);
        if (element == null) {
            throw new NoSuchElementException("Can't get element from list: there's no element at index " + index);
        }
        return element;
    }
}
