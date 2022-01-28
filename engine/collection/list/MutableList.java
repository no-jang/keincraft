package engine.collection.list;

import engine.collection.stack.MutableStack;
import engine.util.Conditions;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface MutableList<T> extends List<T>, MutableStack<T> {
    @Nullable
    T set(int index, T element);

    @Nullable
    T removeOrNull(int index);

    default T replace(int index, T element) {
        T previousElement = set(index, element);
        Conditions.elementNotNull(previousElement, "Can't replace element in list: previous element is null");
        return previousElement;
    }

    default T remove(T element) {
        Conditions.argumentNotNull(element, "Can't remove element from list: element is null");
        return remove(indexOf(element));
    }

    default T remove(int index) {
        T element = removeOrNull(index);
        Conditions.elementNotNull(element, "Can't remove element from list: element index " + index + " out of range");
        return element;
    }

    default void removeAll(Iterable<T> iterable) {
        Conditions.elementNotNull(iterable, "Can't remove elements from list: iterable is null");
        for (T element : iterable) {
            remove(element);
        }
    }
}
