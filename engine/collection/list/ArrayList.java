package engine.collection.list;

import engine.collection.util.ArrayUtil;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Iterator;
import java.util.ListIterator;

public class ArrayList<T> implements MutableList<T> {
    private static final int DEFAULT_CAPACITY = 10;

    private T[] array;
    private int size;

    public ArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayList(int defaultCapacity) {
        array = ArrayUtil.unsafeCastNewArray(defaultCapacity);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    @Nullable
    public T getOrNull(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Can't get element from list: index is negative");
        }

        if (index >= size) {
            return null;
        }

        return array[index];
    }

    @Override
    public void add(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Can't set element in list: element is null");
        }

        array = ArrayUtil.ensureCapacity(array, size, 1);
        array[size] = element;
        size++;
    }

    @Override
    public T set(int index, T element) {
        if (element == null) {
            throw new IllegalArgumentException("Can't set element in list: element is null");
        }

        if (index < 0) {
            throw new IllegalArgumentException("Can't set element in list: index is negative");
        }

        if (index > size) {
            throw new IllegalArgumentException("Can't set element in list: there would be gaps with null elements");
        }

        if (index == size) {
            array = ArrayUtil.ensureCapacity(array, size, 1);
            size++;
        }

        T previousElement = array[index];
        array[index] = element;
        return previousElement;
    }

    @Override
    @Nullable
    public T removeOrNull(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Can't remove element in list: index is negative");
        }

        if (index >= size) {
            return null;
        }

        T previousElement = array[index];

        int startIndex = index + 1;
        for (int i = startIndex; i < size; i++) {
            array[i - 1] = array[i];
            array[i] = null;
        }

        size--;
        return previousElement;
    }

    @Override
    public Iterator<T> iterator() {
        return new ListIterator<>() {
            private int currentIndex = -1;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public T next() {
                T element = array[nextIndex()];
                currentIndex++;
                return element;
            }

            @Override
            public boolean hasPrevious() {
                return currentIndex > 0;
            }

            @Override
            public T previous() {
                T element = array[previousIndex()];
                currentIndex--;
                return element;
            }

            @Override
            public int nextIndex() {
                return currentIndex + 1;
            }

            @Override
            public int previousIndex() {
                return currentIndex - 1;
            }

            @Override
            public void remove() {
                removeOrNull(currentIndex);
            }

            @Override
            public void set(T element) {
                ArrayList.this.set(currentIndex, element);
            }

            @Override
            public void add(T element) {
                ArrayList.this.add(element);
            }
        };
    }
}
