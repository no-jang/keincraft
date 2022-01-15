package engine.collection.iteration;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.NoSuchElementException;

/**
 * An extended {@link java.util.Iterator} interface for iterating over elements. An Iterator can't iterate over null
 * elements.
 *
 * @param <T> type of the elements the {@link Iterator} returns
 */
public interface Iterator<T> extends java.util.Iterator<T> {

    /**
     * Returns true if the next element exists and can be returned. Mainly used to test if a loop should process
     * the next element or not.
     *
     * @return true if next element exists.
     */
    @Override
    boolean hasNext();

    /**
     * Returns the next element or null if the iteration is finished
     *
     * @return the next element or null
     */
    @Nullable
    T nextOrNull();

    /**
     * Resets the current position of the iteration to the first element
     */
    void reset();

    /**
     * Returns the next element or throws a {@link NoSuchElementException} if the iteration is finished. If iterating
     * over a null object a {@link NullPointerException} will be thrown.
     *
     * @return the next element
     * @throws NoSuchElementException If there is no next element available.
     * @throws NullPointerException   If the next element is null.
     */
    @Override
    default T next() {
        T next = nextOrNull();
        if (next == null) {
            throw new NoSuchElementException("Can't iterate over next element: there is nothing left");
        }
        return next;
    }
}
