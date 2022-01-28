package engine.collection.list;

import engine.collection.stack.Stack;
import engine.util.Conditions;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface List<T> extends Stack<T> {
    int indexOf(T element);

    @Nullable
    T getOrNull(int index);

    @Nullable
    T getTailOrNull();

    default boolean contains(T element) {
        Conditions.argumentNotNull(element, "Can't check containment from list: element is null");
        return indexOf(element) >= 0;
    }

    default boolean containsAll(Iterable<T> iterable) {
        Conditions.argumentNotNull(iterable, "Can't check containment from list: iterable is null");
        for (T element : iterable) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    default T get(int index) {
        T element = getOrNull(index);
        Conditions.elementNotNull(element, "Can't get from list: index " + index + " is out of range");
        return element;
    }

    default T getTail() {
        T tail = getTailOrNull();
        Conditions.elementNotNull(tail, "Can't get tail from list: it is empty");
        return tail;
    }
}
