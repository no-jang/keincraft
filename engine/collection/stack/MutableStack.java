package engine.collection.stack;

import engine.collection.sequence.MutableSequence;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface MutableStack<T> extends Stack<T>, MutableSequence<T> {
    void push(T element);

    @Nullable
    T popOrNull();

    default T pop() {
        T element = popOrNull();
        if (element == null) {
            throw new IllegalArgumentException("Can't pop from stack: it is empty");
        }
        return element;
    }
}
