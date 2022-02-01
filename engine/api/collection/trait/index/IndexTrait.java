package engine.api.collection.trait.index;

import org.checkerframework.checker.nullness.qual.Nullable;

public interface IndexTrait<T> {
    int indexOf(T element);

    @Nullable
    T getOrNull(int index);

    default T get(int index) {
        T element = getOrNull(index);

        if (element == null) {
            throw new NullPointerException("Can't get from list: index " + index + " is out of range");
        }

        return element;
    }
}
