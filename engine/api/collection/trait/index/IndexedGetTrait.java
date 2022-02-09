package engine.api.collection.trait.index;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.NoSuchElementException;

public interface IndexedGetTrait<T> {
    @Nullable
    T getOrNull(int index);

    default T get(int index) {
        T element = getOrNull(index);
        if (element == null) {
            throw new NoSuchElementException("Can't get element from collection: no such element at index " + index);
        }
        return element;
    }
}
