package engine.collection.list;

import engine.collection.stack.ArrayStack;
import engine.util.Conditions;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ArrayList<T> extends ArrayStack<T> implements MutableList<T> {
    public ArrayList() {
        super();
    }

    public ArrayList(int defaultCapacity) {
        super(defaultCapacity);
    }

    @Override
    public int indexOf(T element) {
        if (element == null) {
            return -1;
        }

        for (int i = 0; i < size; i++) {
            if (array[i].equals(element)) {
                return i;
            }
        }

        return -1;
    }

    @Nullable
    @Override
    public T getOrNull(int index) {
        Conditions.argumentNotNegative(index, "Can't get element from list: index is negative");
        if (isEmpty() || index > size) {
            return null;
        }
        return array[index];
    }

    @Nullable
    @Override
    public T tailOrNull() {
        if (isEmpty()) {
            return null;
        }

        return array[0];
    }

    @Nullable
    @Override
    public T set(int index, T element) {
        Conditions.argumentNotNegative(index, "Can't set element in list: index is negative");
        Conditions.argumentNotNull(element, "Can't set element in list: element is null");

        ensureCapacity(index);
        T previousElement = array[index];
        array[index] = element;
        return previousElement;
    }

    @Nullable
    @Override
    public T removeOrNull(int index) {
        if (isEmpty() || index > size) {
            return null;
        }
        T previousElement = array[index];
        array[index] = null;
        return previousElement;
    }
}
