package engine.collection.iteration;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Default {@link Iterator} implementation for iterating over an array of elements
 *
 * @param <T> type of array elements
 */
public class ArrayIterator<T> implements Iterator<T> {
    protected final T[] array;
    protected final int size;
    protected int nextIndex;

    /**
     * Creates a new {@link ArrayIterator} with an array and the count of non-null objects behind each other from the
     * beginning.
     * Iterating over null values is not supported and will throw a {@link NullPointerException}.
     *
     * @param array array to iterate over.
     * @param size  the count of non-null objects behind each other from the beginning.
     */
    public ArrayIterator(T[] array, int size) {
        this.array = array;
        this.size = size;
    }

    @Override
    public boolean hasNext() {
        return nextIndex < size;
    }

    @Override
    public void reset() {
        nextIndex = 0;
    }

    @Override
    @Nullable
    public T nextOrNull() {
        if (!hasNext()) {
            return null;
        }

        T element = array[nextIndex++];
        if (element == null) {
            throw new NullPointerException("Iterator can't iterate over null values");
        }

        return element;
    }
}
