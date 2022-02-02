package engine.common.collection.stack;

import engine.api.collection.identificator.Identificator;
import engine.api.collection.stack.MutableStack;
import engine.api.collection.strategy.ArrayStrategy;
import engine.common.collection.identificator.DefaultIdentificator;
import engine.common.collection.strategy.DefaultArrayStrategy;
import engine.common.util.Conditions;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Iterator;

public class ArrayStack<T> implements MutableStack<T> {
    protected T[] array;
    protected int size;

    protected ArrayStrategy<T> strategy;
    protected Identificator<T> identificator;

    public ArrayStack() {
        this(new DefaultArrayStrategy<>(), new DefaultIdentificator<>());
    }

    public ArrayStack(ArrayStrategy<T> strategy, Identificator<T> identificator) {
        this.strategy = strategy;
        this.identificator = identificator;

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

    public static class Builder<T> {
        @Nullable
        protected ArrayStrategy<T> strategy;
        @Nullable
        protected Identificator<T> identificator;

        public Builder() {
        }

        public Builder<T> withStrategy(ArrayStrategy<T> strategy) {
            this.strategy = strategy;
            return this;
        }

        public Builder<T> withIdentificator(Identificator<T> identificator) {
            this.identificator = identificator;
            return this;
        }

        public ArrayStack<T> build() {
            if (strategy == null) {
                strategy = new DefaultArrayStrategy<>();
            }

            if (identificator == null) {
                identificator = new DefaultIdentificator<>();
            }

            return new ArrayStack<>(strategy, identificator);
        }
    }
}
