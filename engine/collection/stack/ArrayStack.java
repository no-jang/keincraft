package engine.collection.stack;

import engine.collection.util.Arrays;
import engine.util.Conditions;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Iterator;

public class ArrayStack<T> implements MutableStack<T> {
    protected static final int DEFAULT_CAPACITY = 10;

    protected T[] array;
    protected int size;

    public ArrayStack() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayStack(int defaultCapacity) {
        this.size = 0;
        this.array = Arrays.unsafeCastNewArray(defaultCapacity);
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
    public void push(T element) {
        Conditions.argumentNotNull(element, "Can't push element on stack: it is null");
        ensureCapacity(size);
        array[size++] = element;
    }

    @Override
    @Nullable
    public T popOrNull() {
        if (isEmpty()) {
            return null;
        }

        int index = size - 1;
        T element = array[index];
        array[index] = null;
        size--;

        return element;
    }

    @Override
    public void clear() {
        size = 0;
        array = Arrays.unsafeCastNewArray(0);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return size > index;
            }

            @Override
            public T next() {
                return array[index++];
            }
        };
    }

    protected void ensureCapacity(int index) {
        array = Arrays.ensureCapacityAtIndex(array, size, index);
    }
}
