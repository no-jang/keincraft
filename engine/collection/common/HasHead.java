package engine.collection.common;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.NoSuchElementException;

/**
 * This interface indicates that the head (first element) of a collection can be fetched.
 *
 * @param <T> type of the elements
 */
public interface HasHead<T> {

    /**
     * Returns the first element or null if the collection is empty
     *
     * @return first element or null
     */
    @Nullable
    T getHeadOrNull();

    /**
     * Return the first element or throws a {@link NoSuchElementException} if {@link #getHeadOrNull()} returned null.
     *
     * @return first element
     * @throws NoSuchElementException If the collection is empty and so no head exists.
     */
    default T getHead() {
        T first = getHeadOrNull();
        if (first == null) {
            throw new NoSuchElementException("Can't get first element of collection: it is empty");
        }
        return first;
    }
}
