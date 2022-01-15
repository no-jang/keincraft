package engine.collection.common;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.NoSuchElementException;

/**
 * This interface indicates that the tail (last element) of a collection can be fetched.
 *
 * @param <T> type of the element
 */
public interface HasTail<T> {

    /**
     * Returns the last element or null if the collection is empty.
     *
     * @return last element or null
     */
    @Nullable
    T getTailOrNull();

    /**
     * Returns the last element or throws a {@link NoSuchElementException} if {@link #getTailOrNull()} is null.
     *
     * @return last element
     * @throws NoSuchElementException If the collection is empty and so no tail exists.
     */
    default T getTail() {
        T last = getTailOrNull();
        if (last == null) {
            throw new NoSuchElementException("Can't get last element of collection: it is empty");
        }

        return last;
    }
}
