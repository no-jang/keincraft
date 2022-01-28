package engine.collection.list;

import engine.collection.stack.Stack;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.NoSuchElementException;

public interface List<T> extends Stack<T> {
    int indexOf(T element);

    @Nullable
    T getOrNull(int index);

    default T get(int index) {
        T element = getOrNull(index);
        if (element == null) {
            throw new NoSuchElementException("Can't get from collection: index " + index + " is out of range");
        }
        return element;
    }

    @Nullable
    T getTailOrNull();

    default T getTail() {
        T tail = getTailOrNull();
        if (tail == null) {
            throw new NoSuchElementException("Can't get tail from collection: it is empty");
        }
        return tail;
    }
}
