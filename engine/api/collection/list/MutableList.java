package engine.api.collection.list;

import engine.api.collection.stack.MutableStack;
import engine.common.util.Conditions;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.NoSuchElementException;

public interface MutableList<T> extends List<T>, MutableStack<T> {
    @Nullable
    T set(int index, T element);

    @Nullable
    T removeOrNull(int index);

    default T replace(int index, T element) {
        T previousElement = set(index, element);
        if (previousElement == null) {
            remove(index);
            throw new NoSuchElementException("Can't replace element in list: previous element is null");
        }
        return previousElement;
    }

    default T remove(T element) {
        Conditions.argumentNotNull(element, "Can't remove element from list: element is null");
        return remove(indexOf(element));
    }

    default T remove(int index) {
        T element = removeOrNull(index);

        if (element == null) {
            throw new NoSuchElementException("Can't remove element from list: element index " + index + " out of range");
        }

        return element;
    }

    default void removeAll(Iterable<T> iterable) {
        Conditions.argumentNotNull(iterable, "Can't remove elements from list: iterable is null");

        for (T element : iterable) {
            remove(element);
        }
    }
}
