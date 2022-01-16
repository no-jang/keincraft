package engine.collection.common;

/**
 * This interface indicates that a collection has a size
 */
public interface Sized {

    /**
     * Returns the size of the collection reaching from 0 to {@link Integer#MAX_VALUE}.
     *
     * @return size of collection
     */
    int size();

    /**
     * True if the size of the collection is 0 and so no elements are present in the collection.
     *
     * @return true if collection is empty
     */
    default boolean isEmpty() {
        return size() <= 0;
    }

    /**
     * True if the size of the collection is greater than 0 and so elements are present.
     *
     * @return true if collection is not empty
     */
    default boolean isNonEmpty() {
        return size() > 0;
    }
}
