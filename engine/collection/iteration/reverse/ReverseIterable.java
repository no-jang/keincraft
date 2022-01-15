package engine.collection.iteration.reverse;

import engine.collection.iteration.Iterable;
import engine.collection.iteration.Iterator;

/**
 * An {@link Iterable} that can start from the end of the collection
 *
 * @param <T> type of elements
 */
public interface ReverseIterable<T> extends Iterable<T> {

    /**
     * {@link Iterator} that starts at the end of the collection
     *
     * @return an {@link Iterator}
     */
    Iterator<T> reverseIterator();
}
