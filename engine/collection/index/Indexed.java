package engine.collection.index;

/**
 * A collection which elements are assigned by an index (position).
 *
 * @param <T> type of returning elements
 */
public interface Indexed<T> {

    /**
     * Finds the index (position). Returns -1 if no element was found.
     *
     * @param element element to search for
     * @return index of element. -1 if not found.
     */
    int indexOf(T element);
}
