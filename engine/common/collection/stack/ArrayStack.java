package engine.common.collection.stack;

import engine.api.collection.stack.MutableStack;
import engine.api.collection.strategy.ArrayStrategy;
import engine.common.collection.strategy.DefaultArrayStrategy;
import engine.common.util.Conditions;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Iterator;

public class ArrayStack<T> implements MutableStack<T> {
    protected T[] array;
    protected int size;

    protected ArrayStrategy<T> strategy;

    public ArrayStack() {
        this(new DefaultArrayStrategy<>());
    }

    public ArrayStack(ArrayStrategy<T> strategy) {
        this.strategy = strategy;

        this.array = strategy.newArray();
    }

    @Override
    @Nullable
    public T headOrNull() {
        if (isEmpty()) {
            return null;
        }

        return array[size - 1];
    }

    @Override
    public void push(T element) {
        Conditions.argumentNotNull(element, "Can't push element on stack: it is null");
        array = strategy.growArray(array, size, 1);
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
        array = strategy.shrinkArray(array, size, 1);

        return element;
    }

    @Override
    public void clear() {
        size = 0;
        array = strategy.newArray();
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
}