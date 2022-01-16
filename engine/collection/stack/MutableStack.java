package engine.collection.stack;

import engine.collection.MutableOrderedCollection;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.NoSuchElementException;

/**
 * A {@link Stack} that can be modified. The last element added is always the first element removed.
 *
 * @param <T> type of containing elements
 */
public interface MutableStack<T> extends Stack<T>, MutableOrderedCollection<T> {
    /**
     * Pushes one element on the end of the stack.
     *
     * @param element element to push
     */
    void push(T element);

    /**
     * Returns and removes one element from the end of the stack. If the stack is empty null will return.
     *
     * @return element or null.
     */
    @Nullable
    T popOrNull();

    /**
     * Returns and remove one element from the end of the stack. If the stack is empty a {@link NoSuchElementException}
     * will throw.
     *
     * @return element
     * @throws NoSuchElementException If stack is empty.
     */
    default T pop() {
        T pop = popOrNull();
        if (pop == null) {
            throw new NoSuchElementException("Can't pop element from stack: it is empty");
        }
        return pop;
    }
}
