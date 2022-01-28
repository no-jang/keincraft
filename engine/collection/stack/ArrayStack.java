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
        Conditions.notNull(element, "Can't push element on stack: it is null");

        array = Arrays.ensureCapacity(array, size, 1);
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
    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }
}
