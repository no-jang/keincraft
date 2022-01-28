package engine.collection.list;

import engine.collection.stack.MutableStack;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.NoSuchElementException;

public interface MutableList<T> extends List<T>, MutableStack<T> {
    void add(T element);

    @Nullable
    T set(int index, T element);

    @Nullable
    T removeOrNull(int index);

    default void addAll(Iterable<T> iterable) {
        for (T element : iterable) {
            add(element);
        }
    }

    default void remove(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Can't remove element from list: it is null");
        }

        for (int i = 0; i < size(); i++) {
            if (element.equals(getOrNull(i))) {
                removeOrNull(i);
            }
        }
    }

    default T remove(int index) {
        T element = removeOrNull(index);
        if (element == null) {
            throw new NoSuchElementException("Can't remove element from list: element does not exist at index " + index);
        }
        return element;
    }

    default void removeAll(Iterable<T> iterable) {
        for (T element : iterable) {
            remove(element);
        }
    }
}
