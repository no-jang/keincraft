package engine.collection.stack;

import engine.collection.builder.AbstractArrayCollectionBuilder;
import engine.collection.util.ArrayUtil;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * An {@link ArrayStack} and {@link MutableStack} that can be modified.
 *
 * @param <T> type of elements
 */
public class MutableArrayStack<T> extends ArrayStack<T> implements MutableStack<T> {
    /**
     * Creates new empty {@link MutableArrayStack} with an initial capacity of 10.
     */
    public MutableArrayStack() {
        super();
    }

    /**
     * Creates new empty {@link MutableArrayStack} with given initial capacity
     *
     * @param initialCapacity initial capacity
     */
    public MutableArrayStack(int initialCapacity) {
        super(initialCapacity);
    }

    // Only be used internally
    protected MutableArrayStack(T[] array, int size) {
        super(array, size);
    }

    @Override
    public void push(T element) {
        array = ArrayUtil.ensureCapacity(array, size, 1);
        array[size++] = element;
    }

    @Override
    public @Nullable T popOrNull() {
        if (isEmpty()) {
            return null;
        }

        return array[(size--) - 1];
    }

    @Override
    public MutableStack<T> toMutable() {
        return this;
    }

    public static class Builder<T> extends AbstractArrayCollectionBuilder<T, MutableArrayStack<T>> {
        public Builder() {
            super();
        }

        public Builder(int initialCapacity) {
            super(initialCapacity);
        }

        @Override
        public MutableArrayStack<T> build() {
            int size = elements.size();
            return new MutableArrayStack<>(elements.toArray(ArrayUtil.unsafeCastNewArray(size)), size);
        }
    }
}
