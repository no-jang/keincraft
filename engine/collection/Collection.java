package engine.collection;

import engine.collection.common.HasHead;
import engine.collection.common.Sized;
import engine.collection.iteration.Iterable;

/**
 * A collection of elements that is {@link Iterable}, has a head element and a size (count of the containing elements).
 * Null elements are not allowed.
 *
 * @param <T> type of containing elements
 */
public interface Collection<T> extends Iterable<T>, HasHead<T>, Sized {

    /**
     * Transforms the {@link Collection} to a {@link MutableCollection}.
     *
     * @return a {@link MutableCollection}
     */
    MutableCollection<T> toMutable();
}
