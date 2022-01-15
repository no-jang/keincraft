package engine.collection.index;

import engine.collection.Collection;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.NoSuchElementException;

/**
 * A collection where elements can be gathered by their index
 *
 * @param <T> type of retuning elements
 */
public interface IndexedCollection<T> extends Collection<T>, Indexed<T> {

    /**
     * Returns the element by its index or null if there's no element on the given index
     *
     * @param index index of the element
     * @return element with given index
     */
    @Nullable
    T getOrNull(int index);

    /**
     * Returns the element by its index ot throws a {@link NoSuchElementException}
     *
     * @param index index of the element
     * @return element with given index
     * @throws NoSuchElementException If there's no element on the given index
     */
    default T get(int index) {
        T get = getOrNull(index);
        if (get == null) {
            throw new NoSuchElementException("Can't get element from collection: it was not found at index");
        }
        return get;
    }
}
