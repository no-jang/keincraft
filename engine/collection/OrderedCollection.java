package engine.collection;

import engine.collection.common.HasTail;
import engine.collection.iteration.reverse.ReverseIterable;

/**
 * An {@link Collection} which elements order is preserved over additions and removals. The last element can be fetched,
 * as well as a {@link engine.collection.iteration.reverse.ReverseIterator} can iterator over it.
 *
 * @param <T> type of containing elements
 */
public interface OrderedCollection<T> extends Collection<T>, HasTail<T>, ReverseIterable<T> {

}
