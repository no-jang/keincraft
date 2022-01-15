package engine.collection.iteration;

/**
 * A collection over which elements you can iterate. Mainly used by the enhanced for-loop syntax.
 * It extends {@link java.lang.Iterable}
 *
 * @param <T> type of elements returned by Iterable
 */
public interface Iterable<T> extends java.lang.Iterable<T> {

    /**
     * {@link Iterator} used for iterating the elements.
     *
     * @return an iterator.
     */
    Iterator<T> iterator();
}
