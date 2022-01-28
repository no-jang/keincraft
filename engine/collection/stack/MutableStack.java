package engine.collection.stack;

import engine.collection.sequence.MutableSequence;
import engine.util.Conditions;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface MutableStack<T> extends Stack<T>, MutableSequence<T> {
    void push(T element);

    @Nullable
    T popOrNull();

    default void pushAll(Iterable<T> iterable) {
        Conditions.argumentNotNull(iterable, "Can't push onto stack: iterable is null");
        for (T element : iterable) {
            push(element);
        }
    }

    default T pop() {
        T element = popOrNull();
        Conditions.elementNotNull(element, "Can't pop from stack: it is empty");
        return element;
    }
}
