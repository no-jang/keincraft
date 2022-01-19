package engine.collection.stack;

import engine.collection.builder.AbstractArrayCollectionBuilder;
import engine.collection.iteration.ArrayIterator;
import engine.collection.iteration.Iterator;
import engine.collection.iteration.reverse.ReverseArrayIterator;
import engine.collection.util.ArrayUtil;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Basic {@link Stack} implementation using an array of elements
 *
 * @param <T> type of returning element
 */
public class ArrayStack<T> implements Stack<T> {
    protected static final int INITIAL_CAPACITY = 10;

    protected T[] array;
    protected int size;

    /**
     * Creates new {@link ArrayStack} with an initial capacity of 10
     */
    public ArrayStack() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Creates new {@link ArrayStack} with given initial capacity
     *
     * @param initialCapacity initial capacity
     */
    public ArrayStack(int initialCapacity) {
        this(ArrayUtil.unsafeCastNewArray(initialCapacity), 0);
    }

    // Only be used internally
    protected ArrayStack(T[] array, int size) {
        this.array = array;
        this.size = size;
    }

    @Override
    @Nullable
    public T getHeadOrNull() {
        if (isEmpty()) {
            return null;
        }

        return array[size - 1];
    }

    @Override
    @Nullable
    public T getTailOrNull() {
        if (isEmpty()) {
            return null;
        }

        return array[0];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int indexOf(T element) {
        for (int i = 0; i < array.length; i++) {
            T arrayElement = array[i];
            if (arrayElement == null) {
                break;
            }

            if (arrayElement.equals(element)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    @Nullable
    public T getOrNull(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Can't peek element from stack: the position is negative");
        }

        if (size - 1 < index) {
            throw new IllegalArgumentException("Can't peek element from stack: the position to peek is greater than the stack size");
        }

        return array[index];
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator<>(array, size);
    }

    @Override
    public Iterator<T> reverseIterator() {
        return new ReverseArrayIterator<>(array, size);
    }

    @Override
    public MutableStack<T> toMutable() {
        return new MutableArrayStack<>(array, size);
    }

    /**
     * Builder for {@link ArrayStack}
     *
     * @param <T> type of {@link ArrayStack} elements
     */
    public static class Builder<T> extends AbstractArrayCollectionBuilder<T, ArrayStack<T>> {
        public Builder() {
            super();
        }

        public Builder(int initialCapacity) {
            super(initialCapacity);
        }

        @Override
        public ArrayStack<T> build() {
            int size = elements.size();
            return new ArrayStack<>(elements.toArray(ArrayUtil.unsafeCastNewArray(size)), size);
        }
    }
}
