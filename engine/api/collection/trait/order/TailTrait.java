package engine.api.collection.trait.order;

import org.checkerframework.checker.nullness.qual.Nullable;

public interface TailTrait<T> {
    @Nullable
    T tailOrNull();

    default T getTail() {
        T tail = tailOrNull();

        if (tail == null) {
            throw new NullPointerException("Can't get tail from collection: it is empty");
        }

        return tail;
    }
}
