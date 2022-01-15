package engine.collection.common;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.NoSuchElementException;

public interface HasTail<T> {
    @Nullable
    T getTailOrNull();

    default T getTail() {
        T last = getTailOrNull();
        if (last == null) {
            throw new NoSuchElementException("Can't get last element of collection: it is empty");
        }

        return last;
    }
}
