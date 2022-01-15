package engine.collection.stack;

import engine.collection.MutableOrderedCollection;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.NoSuchElementException;

public interface MutableStack<T> extends Stack<T>, MutableOrderedCollection<T> {
    void push(T element);

    @Nullable
    T popOrNull();

    default T pop() {
        T pop = popOrNull();
        if (pop == null) {
            throw new NoSuchElementException("Can't pop element from stack: it is empty");
        }
        return pop;
    }
}
