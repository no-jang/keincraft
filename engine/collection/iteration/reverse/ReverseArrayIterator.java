package engine.collection.iteration.reverse;


import engine.collection.iteration.ArrayIterator;

/**
 * An {@link ArrayIterator} and {@link ReverseIterator} that starts at the end of the collection.
 *
 * @param <T> type of elements iterating over
 */
public class ReverseArrayIterator<T> extends ArrayIterator<T> implements ReverseIterator<T> {


    /**
     * Creates a new {@link ReverseArrayIterator} with an array and the count of non-null objects behind each other from the
     * beginning.
     * Iterating over null values is not supported and will throw a {@link NullPointerException}.
     *
     * @param array array to iterate over.
     * @param size  the count of non-null objects behind each other from the beginning.
     */
    public ReverseArrayIterator(T[] array, int size) {
        super(array, size);
        nextIndex = size - 1;
    }

    @Override
    public boolean hasNext() {
        return nextIndex >= 0;
    }

    @Override
    public void reset() {
        nextIndex = size - 1;
    }

    @Override
    protected int nextIndex() {
        return nextIndex--;
    }
}
