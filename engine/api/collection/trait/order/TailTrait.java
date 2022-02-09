package engine.api.collection.trait.order;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.NoSuchElementException;

public interface TailTrait<T> {
    @Nullable
    T tailOrNull();

    default T tail() {
        T tail = tailOrNull();
        if (tail == null) {
            throw new NoSuchElementException("Can't get tail from collection: it is empty");
        }
        return tail;
    }
}
